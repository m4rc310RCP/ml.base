package com.m4rc310.ml.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.brazilutils.br.cpfcnpj.CpfCnpj;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.UIEvents;

import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Pessoa;
import com.m4rc310.ml.models.PessoaJuridica;
import com.m4rc310.ml.models.QualGrupo;
import com.m4rc310.ml.models.Socio;
import com.m4rc310.ml.ui.actions.MAction;
import com.m4rc310.rcp.graphql.MGraphQL2;

@Creatable
@Deprecated
public class ActionPeopleRegistration extends MAction {

	public static final String SEND_ACTION_REGISTRATION = "send_action_registration";
	public static final String CHANGE_PART = "change_part";
	public static final String INFORM_LOADING = "inform_loading";
	public static final String LOAD_PEOPLE_TYPES = "load_people_types";
	public static final String LOAD_PESSOA_JURIDICA = "load_pessoa_juridica";
	public static final String SET_PEOPLE_TYPES = "set_people_types";
	public static final String INFORM_ERROR = "inform_error";
	public static final String LOAD_QUAL_GRUPO = "load_qual_grupo";

	@Inject
	@Translation
	M m;

	@Inject
	MGraphQL2 graphQL;

	@Inject
	UISynchronize sync;
	
	

	@Inject
	IEventBroker eventBroker;
	private boolean isLoaded = false;

	public enum EnumPartChange {
		EMPTY_CPF, INVALID_CPF, VALID_CPF, ON_FOCUS, LOADING_PEOPLE
	}

	public void init() {
		fire(CHANGE_PART, EnumPartChange.EMPTY_CPF);
		fire(CHANGE_PART, EnumPartChange.ON_FOCUS);
		fire(LOAD_PEOPLE_TYPES, Arrays.asList(Pessoa.FISICA, Pessoa.JURIDICA, Pessoa.NONE));
		isLoaded = false;
	}

	public void closeing() {
		fire(INFORM_LOADING, false, m.empty);
	}

	public void verifyCpfCnpj(String scc) {
		
		Job job = new Job(m.textCheckingForPeople) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				
				CpfCnpj cc = new CpfCnpj(scc);
				sync.asyncExec(() -> fire(CHANGE_PART, EnumPartChange.LOADING_PEOPLE));
				sync.asyncExec(() -> {
					String text = MessageFormat.format(m.textCheckingForPeople,
							cc.isCnpj() ? m.textPeopleJuridica : m.textPeopleFisica);
					fire(INFORM_LOADING, true, text);
				});
				sync.asyncExec(() -> eventBroker.send(SEND_ACTION_REGISTRATION, ActionPeopleRegistration.this));
				sync.asyncExec(() ->updateHandlers());
				
				try {
					String query = "{pessoaJuridica(scnpj:\"%s\"){cnpj nome fantasia abertura  naturezaJuridica situacao abertura tipo "
							+ "capitalSocial atividadesSecundarias{code text} qsa{qual nome} atividadePrincipal{code text}}}";
					Optional<PessoaJuridica> data = graphQL.query(query, cc.getNumber()).getData("pessoaJuridica",
							PessoaJuridica.class);
					
					PessoaJuridica pj = data.orElse(null);
					
					Map<String, QualGrupo> mapSocios = new HashMap<>();
					if(pj.getQsa()!=null) {
						for (Socio socio : pj.getQsa()) {
							String cod = socio.getQual();
							if(!mapSocios.containsKey(cod)) {
								QualGrupo grupo = new QualGrupo();
								grupo.setCodigo(cod);
								grupo.setDescricao(cod);
								grupo.setSocios(new ArrayList<>());
								mapSocios.put(cod, grupo);
							}
							
							QualGrupo grupo = mapSocios.get(cod);
							grupo.getSocios().add(socio);
						}
					}
					
					
					
					sync.asyncExec(() -> fire(LOAD_QUAL_GRUPO, mapSocios));
					sync.asyncExec(() -> fire(LOAD_PESSOA_JURIDICA, pj));
					sync.asyncExec(() -> fire(INFORM_LOADING, false, m.empty));
					
				} catch (Exception e) {
					
					
					sync.asyncExec(() -> fire(INFORM_LOADING, false, m.empty));
					sync.asyncExec(() -> fire(INFORM_ERROR, m.errorCarregarPessoa));
					eventBroker.send(SEND_ACTION_REGISTRATION, null);
					init();
				}

				
				updateHandlers();
				
				return Status.OK_STATUS;
			}
		};
		

		job.schedule();

//			try {
//				sync.asyncExec(()->{
//					sync.asyncExec(() -> eventBroker.send(SEND_ACTION_REGISTRATION, this));
//					
//					CpfCnpj cc = new CpfCnpj(scc);
//					
//					String text = MessageFormat.format(m.textCheckingForPeople,
//							cc.isCnpj() ? m.textPeopleJuridica : m.textPeopleFisica);
//
//					sync.asyncExec(() -> fire(INFORM_LOADING, true, text));
//					sync.asyncExec(() -> fire(CHANGE_PART, EnumPartChange.LOADING_PEOPLE));
//					String query = "{pessoaJuridica(scnpj:\"%s\"){cnpj nome fantasia abertura  naturezaJuridica situacao atividadesSecundarias{code text} atividadePrincipal{code text}}}";
//					Optional<PessoaJuridica> data = graphQL.query(query, cc.getNumber()).getData("pessoaJuridica",
//							PessoaJuridica.class);
//					
//					sync.asyncExec(() -> fire(INFORM_LOADING, false, m.textEmpty));
//					updateHandlers();
//				});
//				
//				
//				
//			
//			} catch (Exception e) {
//				sync.asyncExec(() -> fire(INFORM_LOADING, false, m.textEmpty));
//				sync.asyncExec(() -> fire(INFORM_ERROR, m.errorCarregarPessoa));
////		eventBroker.send(SEND_ACTION_REGISTRATION, null);
//			}

//			throw new UnsupportedOperationException("Error ");
//			eventBroker.send(SEND_ACTION_REGISTRATION, null);

//		CpfCnpj cc = new CpfCnpj(scc);
//
//		String text = MessageFormat.format(m.textCheckingForPeople,
//				cc.isCnpj() ? m.textPeopleJuridica : m.textPeopleFisica);
//
//		sync.asyncExec(() -> fire(INFORM_LOADING, true, text));
//		sync.asyncExec(() -> fire(CHANGE_PART, EnumPartChange.LOADING_PEOPLE));
//
//		if (cc.isCnpj()) {
//
//			sync.asyncExec(() -> {
//				String query = "{pessoaJuridica(scnpj:\"%s\"){cnpj nome fantasia abertura  naturezaJuridica situacao atividadesSecundarias{code text} atividadePrincipal{code text}}}";
//				Optional<PessoaJuridica> data = graphQL.query(query, cc.getNumber()).getData("pessoaJuridica",
//						PessoaJuridica.class);
//
//				PessoaJuridica pj = data.orElse(null);
//				sync.asyncExec(() -> fire(LOAD_PESSOA_JURIDICA, pj));
//				sync.asyncExec(() -> fire(INFORM_LOADING, false, m.textEmpty));
//
//				sync.asyncExec(() -> eventBroker.send(SEND_ACTION_REGISTRATION, pj == null ? null : this));
//				updateHandlers();
//				isLoaded = true;
//			});
//
//		}
	}

	public void cancel() {
		fire(LOAD_PESSOA_JURIDICA, (PessoaJuridica) null);
		sync.asyncExec(() -> fire(LOAD_QUAL_GRUPO, new HashMap<>()));
		eventBroker.send(SEND_ACTION_REGISTRATION, null);
		init();
		updateHandlers();
		isLoaded = false;
	}

	private void updateHandlers() {
		eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
	}

	public void readingCpfCnpj(String scc) {
		if (isLoaded) {
			return;
		}

		if (scc.isEmpty()) {
			fireInCache(CHANGE_PART, EnumPartChange.EMPTY_CPF);
			return;
		}
		CpfCnpj cc = new CpfCnpj(scc);
		fireInCache(CHANGE_PART, cc.isValid() ? EnumPartChange.VALID_CPF : EnumPartChange.INVALID_CPF);
		if (cc.isCnpj()) {
			fireInCache(SET_PEOPLE_TYPES, Pessoa.JURIDICA);
		}

		if (cc.isCpf()) {
			fireInCache(SET_PEOPLE_TYPES, Pessoa.FISICA);
		}

		if (!cc.isValid()) {
			fireInCache(SET_PEOPLE_TYPES, Pessoa.NONE);
		}

	}
}
