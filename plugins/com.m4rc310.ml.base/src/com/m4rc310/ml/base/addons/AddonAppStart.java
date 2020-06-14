package com.m4rc310.ml.base.addons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.osgi.service.event.Event;

import com.m4rc310.ml.base.actions.ActionCadastroML;
import com.m4rc310.ml.base.actions.ActionSales;
import com.m4rc310.ml.base.i18n.M;
import com.m4rc310.ml.base.models.Processo;
import com.m4rc310.rcp.popup.notifications.MPopupNotification;

@SuppressWarnings("restriction")
public class AddonAppStart {

	@Inject
	MApplication application;
	@Inject
	EPartService partService;
	@Inject
	EModelService modelService;

	@Inject
	IEventBroker eventBroker;

	@Inject
	ActionCadastroML action;

	@Inject
	ActionSales actionSales;

	@Inject
	MPopupNotification popup;

	@Inject
	@Translation
	M m;

	private final Map<String, MPart> partsMap = new HashMap<>();

	@Inject
	UISynchronize sync;

//	@Inject PartControl pc;

	@Inject
	@Optional
	public void applicationStarted(@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {

		actionSales.addListener(ActionSales.LOAD_SALES, e -> {
			List<Processo> sales = e.getListValue(0, Processo.class);

			for (Processo processo : sales) {
				showSalePart(processo);
			}
			
			closeCompletedSalesParts(sales);
		});

		actionSales.addListener(ActionSales.LOAD_SALE, e -> {
			Processo sale = e.getValue(Processo.class);
			showSalePart(sale);
		});

//		action.addListener(this, ActionCadastroML.INFORME_MESSAGE, e->{
//			String erro = e.getValue(String.class);
//			popup.show("Erro de Conecção", erro);
//		});
		action.configure();
		actionSales.searchForPendingSales();
	}

	private void showSalePart(Processo processo) {
		String id = String.format("processo_%d", processo.getId());
		show("com.m4rc310.ml.base.partdescriptor.partsale", id, "", processo);
	}
	
	public void closeCompletedSalesParts(List<Processo> processos) {
		partsMap.forEach((con, part)->{
			
			boolean remove = true;
			
			for (Processo p : processos) {
				String key = String.format("processo_%d", p.getId());
				if(key.equals(con)) {
					remove = false;
					part.setObject(p);
					break;
				}
			}
			
			if(remove) {
				partService.hidePart(part, true);
			}else {
				show("com.m4rc310.ml.base.partdescriptor.partsale", con, "", part.getObject());
			}
		});
	}
	

	public void show(String partUri, String partId, String title, Object value) {

		sync.syncExec(() -> {
			try {
				MPart part;

				if (partsMap.containsKey(partId)) {
					part = partsMap.get(partId);
					part.setObject(value);

					for (String variable : part.getVariables()) {
						if (variable.contains("partStack:")) {
							variable = variable.replace("partStack:", "");
							MPartStack stack = modelService.findElements(application, variable, MPartStack.class, null)
									.get(0);
							stack.setVisible(true);
							List<MStackElement> childres = stack.getChildren();
							childres.add(part);
							break;
						}
					}

					partService.showPart(part, PartState.ACTIVATE);
					if (value != null) {
						eventBroker.send("update_part_report", part);
					}

					return;
				}

				part = partService.createPart(partUri);
				part.setElementId(partId);

				if (title != null) {
					part.setLabel(title);
				}

				part.setObject(value);

				for (String variable : part.getVariables()) {
					if (variable.contains("partStack:")) {
						variable = variable.replace("partStack:", "");
						MPartStack stack = modelService.findElements(application, variable, MPartStack.class, null)
								.get(0);
						stack.setVisible(true);
						List<MStackElement> childres = stack.getChildren();
						childres.add(part);
						break;
					}
				}

				partService.showPart(part, PartState.ACTIVATE);
				eventBroker.send("update_part_report", part);

				partsMap.put(partId, part);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});

	}

}
