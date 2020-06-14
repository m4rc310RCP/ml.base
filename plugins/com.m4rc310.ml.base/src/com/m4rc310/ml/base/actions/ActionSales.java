package com.m4rc310.ml.base.actions;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;

import com.m4rc310.ml.base.i18n.M;
import com.m4rc310.ml.base.models.Processo;
import com.m4rc310.ml.base.utils.HardwareInfo;
import com.m4rc310.rcp.graphql.MGraphQL2;
import com.m4rc310.rcp.ui.utils.MAction;

@Creatable
@Singleton
public class ActionSales extends MAction {
	public static final String LOAD_SALES = "load_sales";
	public static final String LOAD_SALE = "load_sale";
	private static final String UPDATE_SALES = "update_sales";

	private String serial = HardwareInfo.getSerialNumber();

	@Inject
	@Translation
	M m;

	@Inject
	private MGraphQL2 graphql;
	
//	@Inject PartControl pc;

	@Inject
	ActionStatus actionStatus;
	
	public void newSale() {
		try {
			String query = "mutation{abrirProcessoVenda(deviceId:\"%s\"){id dataInicio dataFim}}";
			graphql.query(query, serial);
			graphql.getData("abrirProcessoVenda", Processo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

	public void waitForPendingSales() {
		String topic = String.format("new_sale_%s", serial);
		graphql.subscribe(topic, e -> {
			Processo processo = e.getContent(Processo.class);
			fire(LOAD_SALE, processo);
		});
		
		topic = String.format("update_sales_%s", serial);
		graphql.subscribe(topic, e -> {
			fire(UPDATE_SALES, e.getContentList(Processo.class));
		});
		
	}

	public void searchForPendingSales() {
		try {
			actionStatus.status(m.textSearchingForSales);

			String query = "{processos(pendentes:true deviceId:\"%s\"){id dataInicio dataFim}}";
			graphql.query(query, serial);
			List<Processo> processos = graphql.getDataList("processos", Processo.class);
			fire(LOAD_SALES, processos);

			waitForPendingSales();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
