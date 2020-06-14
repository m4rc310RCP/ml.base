package com.m4rc310.ml.actions;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import com.m4rc310.ml.actions.ActionSearch.EnumSearch;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Atividade;
import com.m4rc310.ml.models.Pessoa;
import com.m4rc310.ml.models.PessoaJuridica;
import com.m4rc310.ml.models.QualGrupo;
import com.m4rc310.ml.models.Socio;
import com.m4rc310.ml.models.Vinculo;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar.IconType;
import com.m4rc310.ml.ui.actions.MAction;
import com.m4rc310.rcp.graphql.GraphQLQueryException;
import com.m4rc310.rcp.graphql.MGraphQL2;

@Creatable
public class ActionPessoa extends MAction {

	@Inject
	ActionStatusBar actionStatusBar;
	
	@Inject ActionSearch actionSearch;

	@Inject
	MGraphQL2 graphQL;

	@Inject
	UISynchronize sync;

	@Inject
	@Translation
	M m;

	@Inject
	IEventBroker eventBroker;

	private static final String PJ_QUERY_RESPONSE = "cnpj nome fantasia abertura  naturezaJuridica situacao abertura tipo\n"
			+ "									capitalSocial atividadesSecundarias{code text} qsa{qual nome} atividadePrincipal{code text}\n"
			+ "									logradouro numero bairro municipio cep uf vinculo{data id}";

	public static final String CHANGE_PART = "change_parte_pessoa";

	public static final String SEND_ACTION_REGISTRATION = "send_action_registration";

	public enum EnumPart {
		PREPARE_TO_LOAD_PESSOA, LOAD_TYPE_PESSOAS, RESET, AVANCAR, SET_PESSOA, AVANCAR_SEM_CC, PART_TO_PESSOA,
		ENABLE_CONFIRM_NO_CC, LOCK_FOR_EDITION, EDIT_PJ, EDIT_PF, SHOW_ERROR_DIALOG, WAITING, LOAD_PESSOA_PF,
		LOAD_PESSOA_PJ, LOAD_QSA, ENABLE_VINCULO, LOAD_VINCULO, QUESTION_DESVINCULAR, CHANGE_PART_TITLE
	}

	public void cancelar() {
		init();
	}

	public void escreverCpfCnpj(String scc) {

		CpfCnpj cc = new CpfCnpj(scc);
		if (cc.isValid()) {
			int pessoa = cc.isCnpj() ? Pessoa.JURIDICA : Pessoa.FISICA;
			fire(CHANGE_PART, EnumPart.PREPARE_TO_LOAD_PESSOA, pessoa);
			fire(CHANGE_PART, EnumPart.SET_PESSOA, pessoa);
			fire(CHANGE_PART, EnumPart.PART_TO_PESSOA, pessoa);
		} else {
			int pessoa = Pessoa.NONE;
			fire(CHANGE_PART, EnumPart.PREPARE_TO_LOAD_PESSOA, pessoa);
			fire(CHANGE_PART, EnumPart.SET_PESSOA, pessoa);
			fire(CHANGE_PART, EnumPart.PART_TO_PESSOA, pessoa);
		}
		fire(CHANGE_PART, EnumPart.ENABLE_CONFIRM_NO_CC, false);
	}

	public void init() {
		fire(CHANGE_PART, EnumPart.RESET);
		fire(CHANGE_PART, EnumPart.LOAD_TYPE_PESSOAS, Arrays.asList(Pessoa.FISICA, Pessoa.JURIDICA, Pessoa.NONE));
		fire(CHANGE_PART, EnumPart.ENABLE_CONFIRM_NO_CC, false);
//		fire(CHANGE_PART, EnumPart.LOCK_FOR_EDITION, true);
		fire(CHANGE_PART, EnumPart.LOAD_PESSOA_PF, null);
		fire(CHANGE_PART, EnumPart.LOAD_PESSOA_PJ, null);
		fire(CHANGE_PART, EnumPart.SET_PESSOA, Pessoa.NONE);
		fire(CHANGE_PART, EnumPart.ENABLE_VINCULO, false);
		fire(CHANGE_PART, EnumPart.LOAD_VINCULO, null);
		fire(CHANGE_PART, EnumPart.CHANGE_PART_TITLE, m.titleDefaultCadastroPessoa);

		eventBroker.send(SEND_ACTION_REGISTRATION, null);
		updateHandlers();
		
		actionStatusBar.setLeftMessage(IconType.EMOTICON_FACE_GLASSES, m.infoCadastroPessoa);
		
		actionSearch.addListener(this, ActionSearch.SEARCH_ACTION, e->{
			EnumSearch enumSearch = e.getValue(0, EnumSearch.class);
			switch (enumSearch) {
			case PROCESS_SEARCH:
				String text = e.getValue(1, String.class);
//				boolean isphonetic= e.getValue(2, boolean.class);
//				List<Object> values = e.getListValue(3, Object.class);
				
				try {
					String query = "{ searchPJ(ref:\"%s\"){%s}}";
					List<PessoaJuridica> dataList = graphQL.query(query, text, PJ_QUERY_RESPONSE).getDataList("searchPJ", PessoaJuridica.class);
					actionSearch.fire(ActionSearch.SEARCH_ACTION, EnumSearch.RESULT_OF_SEARCH, dataList);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				break;
			default:
				break;
			}
		});
		
		actionSearch.addListener(this, ActionSearch.RETURN_VALUE, e->{
			PessoaJuridica pj = e.getValue(PessoaJuridica.class);
			Vinculo vinculo = pj.getVinculo();
			NumberFormat format = NumberFormat.getNumberInstance();
			String matricula = String.format("%s-%s", 
					format.format(vinculo.getId()), getMod10(vinculo.getId()+""));
			vinculo.setMatricula(matricula);

			fire(CHANGE_PART, EnumPart.LOAD_PESSOA_PJ, pj);
			fire(CHANGE_PART, EnumPart.LOAD_VINCULO, vinculo);
		});

	}

	public void close() {
		actionStatusBar.setLeftMessage(m.empty);
	}

	public void avancarSemCC() {
		fire(CHANGE_PART, EnumPart.AVANCAR_SEM_CC);
	}

	private void mudarTituloPartPJ(PessoaJuridica pj) {
		String title = m.titleDefaultCadastroPessoa;
		if (pj != null) {
//			title= pj.getFantasia().isEmpty()?pj.getNome():pj.getFantasia();
			title = pj.getNome();
		}
		fire(CHANGE_PART, EnumPart.CHANGE_PART_TITLE, title);
	}

	public void avancar(String scc) {
		Job job = new Job(m.textCheckingForPeople) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				CpfCnpj cc = new CpfCnpj(scc);
				if (cc.isValid()) {

					sync.syncExec(() -> fire(CHANGE_PART, EnumPart.WAITING, true, m.textCheckingForPeople));

					try {
						if (cc.isCnpj()) {
							String query = "{pessoaJuridica(scnpj:\"%s\"){%s}}";
							Optional<PessoaJuridica> data = graphQL.query(query, cc.getNumber(), PJ_QUERY_RESPONSE)
									.getData("pessoaJuridica", PessoaJuridica.class);

							PessoaJuridica pj = data.orElse(null);
							fire(CHANGE_PART, EnumPart.ENABLE_VINCULO, pj != null);
							mudarTituloPartPJ(pj);

							if (pj != null) {
								if (pj.getCapitalSocial() != null) {
									Locale locale = new Locale("pt", "BR");
									NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
									pj.setScapitalSocial(numberFormat.format(pj.getCapitalSocial()));
								}

								Vinculo vinculo = pj.getVinculo();
								if(vinculo!=null) {
								

								NumberFormat format = NumberFormat.getNumberInstance();
								String matricula = String.format("%s-%s", 
										format.format(vinculo.getId()), getMod10(vinculo.getId()+""));
								vinculo.setMatricula(matricula);
								}

								fire(CHANGE_PART, EnumPart.LOAD_VINCULO, vinculo);
							}

							Atividade ap = pj.getAtividadePrincipal().get(0);
							String sap = String.format("%s - %s", ap.getCode(), ap.getText());

							pj.setSatividadePrincipal(sap);

							Map<String, QualGrupo> mapSocios = new HashMap<>();
							if (pj.getQsa() != null) {
								for (Socio socio : pj.getQsa()) {
									String cod = socio.getQual();
									if (!mapSocios.containsKey(cod)) {
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

							fire(CHANGE_PART, EnumPart.LOAD_QSA, mapSocios);
							fire(CHANGE_PART, EnumPart.LOAD_PESSOA_PJ, pj);
							fire(CHANGE_PART, EnumPart.LOCK_FOR_EDITION, true);
							fire(CHANGE_PART, EnumPart.EDIT_PJ);
							fire(CHANGE_PART, EnumPart.AVANCAR);

							eventBroker.send(SEND_ACTION_REGISTRATION, ActionPessoa.this);
							updateHandlers();

						}

						if (cc.isCpf()) {

						}
						sync.asyncExec(() -> fire(CHANGE_PART, EnumPart.WAITING, false, m.empty));
					} catch (Exception e) {
						e.printStackTrace();
						GraphQLQueryException error = graphQL.getError(e);
						fire(CHANGE_PART, EnumPart.SHOW_ERROR_DIALOG, error.getMessage());
						sync.asyncExec(() -> fire(CHANGE_PART, EnumPart.WAITING, false, m.empty));
						return Status.CANCEL_STATUS;
					}

				} else {
					fire(CHANGE_PART, EnumPart.SHOW_ERROR_DIALOG, m.errorCarregarPessoa);
				}

				return Status.OK_STATUS;
			}
		};

		job.schedule();
	}

	public void vincular(String scc) {
		Job job = new Job(m.textWaiting) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					sync.syncExec(() -> fire(CHANGE_PART, EnumPart.WAITING, true, m.textWaiting));
					String query = "mutation{criarVinculo(scc:\"%s\"){%s}}";

					Optional<PessoaJuridica> data = graphQL.query(query, scc, PJ_QUERY_RESPONSE).getData("criarVinculo",
							PessoaJuridica.class);
					if (data.isPresent()) {

						PessoaJuridica pf = data.get();

						Vinculo vinculo = pf.getVinculo();
						NumberFormat format = NumberFormat.getNumberInstance();
						String matricula = String.format("%s-%s", 
								format.format(vinculo.getId()), getMod10(vinculo.getId()+""));
						vinculo.setMatricula(matricula);

						fire(CHANGE_PART, EnumPart.LOAD_PESSOA_PJ, pf);
						fire(CHANGE_PART, EnumPart.LOAD_VINCULO, vinculo);
					}

					sync.syncExec(() -> fire(CHANGE_PART, EnumPart.WAITING, false, m.empty));

					return Status.OK_STATUS;
				} catch (Exception e) {
					fire(CHANGE_PART, EnumPart.SHOW_ERROR_DIALOG, e.getMessage());
					sync.syncExec(() -> fire(CHANGE_PART, EnumPart.WAITING, false, m.empty));
					return Status.CANCEL_STATUS;
				}
			}
		};

		job.schedule();

	}

	public void desvincularConfirmar(String scc) {
		String message = m.questionDesvincular;
		CpfCnpj cc = new CpfCnpj(scc);
		message = MessageFormat.format(message, cc.getCpfCnpj());
		fire(CHANGE_PART, EnumPart.QUESTION_DESVINCULAR, message);
	}

	public void desvincular(String scc) {
		Job job = new Job(m.textWaiting) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try {
					sync.syncExec(() -> fire(CHANGE_PART, EnumPart.WAITING, true, m.textWaiting));
					String query = "mutation{removerVinculos(scc:\"%s\"){%s}}";

					Optional<PessoaJuridica> data = graphQL.query(query, scc, PJ_QUERY_RESPONSE)
							.getData("removerVinculos", PessoaJuridica.class);
					if (data.isPresent()) {
						fire(CHANGE_PART, EnumPart.LOAD_PESSOA_PJ, data.get());
						fire(CHANGE_PART, EnumPart.LOAD_VINCULO, data.get().getVinculo());
					}

					sync.syncExec(() -> fire(CHANGE_PART, EnumPart.WAITING, false, m.empty));
					return Status.OK_STATUS;
				} catch (Exception e) {
					fire(CHANGE_PART, EnumPart.SHOW_ERROR_DIALOG, e.getMessage());
					sync.syncExec(() -> fire(CHANGE_PART, EnumPart.WAITING, false, m.empty));
					return Status.CANCEL_STATUS;
				}
			}
		};

		job.schedule();
	}

	private void updateHandlers() {
		eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
	}

	public void mudarPartPessoa(int pessoa) {
		fire(CHANGE_PART, EnumPart.PART_TO_PESSOA, pessoa);
		fire(CHANGE_PART, EnumPart.ENABLE_CONFIRM_NO_CC, pessoa != Pessoa.NONE);
	}

}
