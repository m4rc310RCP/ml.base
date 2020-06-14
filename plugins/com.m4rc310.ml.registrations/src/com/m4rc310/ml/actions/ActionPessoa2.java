package com.m4rc310.ml.actions;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brazilutils.IEUtils;
import org.brazilutils.br.cpfcnpj.CpfCnpj;
import org.brazilutils.br.uf.UF;
//import org.brazilutils.br.uf.ie.InscricaoEstadual;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;

import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Atividade;
import com.m4rc310.ml.models.MInscricaoEstadual;
import com.m4rc310.ml.models.Pessoa;
import com.m4rc310.ml.models.PessoaFisica;
import com.m4rc310.ml.models.PessoaJuridica;
import com.m4rc310.ml.models.QualGrupo;
import com.m4rc310.ml.models.Socio;
import com.m4rc310.ml.models.Vinculo;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar.IconType;
import com.m4rc310.ml.ui.MD5Utils;
import com.m4rc310.ml.ui.actions.MAction;
import com.m4rc310.rcp.graphql.MGraphQL2;

@Creatable
@Singleton
public class ActionPessoa2 extends MAction {

	@Inject
	ActionStatusBar actionStatusBar;

	@Inject
	ActionSearch actionSearch;

	@Inject
	MGraphQL2 graphQL;

	@Inject
	UISynchronize sync;

	@Inject
	@Translation
	M m;

	@Inject
	IEventBroker eventBroker;

	private Job jobChecking;

	private PessoaJuridica pj;

	private PessoaFisica pf;

	private String checksumPF = "";

	private static final String PF_QUERY_RESPONSE = "id nome nascimento cpf sexo estadoCivil";

	private static final String PJ_QUERY_RESPONSE = "cnpj nome fantasia abertura  naturezaJuridica situacao abertura tipo\n"
			+ "									capitalSocial atividadesSecundarias{code text} qsa{qual nome} atividadePrincipal{code text}\n"
			+ "									logradouro numero bairro municipio cep uf vinculo{data id}";

	public static final String RESET_PART = "reset_part";

	public static final String INFORME_VALID_CC = "informe_valid_cc";
	public static final String INFORME_VALID_IE = "informe_valid_ie";

	public static final String IS_LOADED = "is_loaded";

	public static final String LOAD_PJ = "load_pj";
	public static final String LOAD_PF = "load_pf";
	public static final String LOAD_TYPES = "load_types";
	public static final String LOAD_UFS = "load_ufs";
	public static final String LOAD_IE = "load_ie";
	public static final String LOAD_VALUES = "load_values";

	public static final String ENABLE_MANUALLY_DATA = "enable_manually_data";
	public static final String ENABLE_BOND = "enable_aggregation";
	public static final String ENABLE_IE = "enable_ie";
	public static final String ENABLE_DIRTY = "enable_dirty";

	public static final String SEARCHING = "searching";

	public static final String SET_TYPE = "set_type";
	public static final String SET_UF = "set_uf";
	public static final String SET_PART_TITLE = "set_part_title";

	public static final String QUESTION_REMOVE_BOND = "question_remove_bond";
	public static final String PJ_LOCK_EDIT = "pj_lock_edit";

	public enum EnumValues {
		LIST_SEXO, LIST_ESTADO_CIVIL
	}

	public void init() {
		fire(RESET_PART);
		fire(LOAD_TYPES, Arrays.asList(Pessoa.NONE, Pessoa.FISICA, Pessoa.JURIDICA));

		fire(LOAD_VALUES, EnumValues.LIST_SEXO, Arrays.asList(Pessoa.SEXO_FEMININO, Pessoa.SEXO_MASCULINO));
		fire(LOAD_VALUES, EnumValues.LIST_ESTADO_CIVIL,
				Arrays.asList(Pessoa.ESTADO_CIVIL_CASADO, Pessoa.ESTADO_CIVIL_DIVORCIADO,
						Pessoa.ESTADO_CIVIL_SEPARADO_JUDICIAL, Pessoa.ESTADO_CIVIL_SOLTEIRO,
						Pessoa.ESTADO_CIVIL_UNIAO_ESTAVEL, Pessoa.ESTADO_CIVIL_VIUVO));

		fire(PJ_LOCK_EDIT, true);

		fire(LOAD_UFS, UF.values());

		fire(ENABLE_IE, false);
		fire(INFORME_VALID_IE, false);

		fire(LOAD_IE, (MInscricaoEstadual) null);

		loadPF(null);
		loadPJ(null);

		fire(SET_PART_TITLE, m.titleDefaultCadastroPessoa);
	}

	public void searchAndLoad(String scc) {
		CpfCnpj cc = new CpfCnpj(scc);
		if (cc.isCnpj()) {
			searchAndLoadPJ(scc);
		}

		if (cc.isCpf()) {
			searchAndLoadPF(scc);
		}

	}

	public void searchAndLoadPF(String scpf) {
		this.jobChecking = Job.create(m.textCheckingForPeople, monitor -> {
			try {
				sync.asyncExec(() -> fire(SEARCHING, true));
				sync.asyncExec(() -> actionStatusBar.setLeftMessage(IconType.WAIT, m.textCheckingForPeople,
						m.textPeopleFisica));

				String query = "{pessoaFisica(cpf:\"%s\"){%s}}";
				CpfCnpj cc = new CpfCnpj(scpf);
				Optional<PessoaFisica> data = graphQL.query(query, cc.getNumber(), PF_QUERY_RESPONSE)
						.getData("pessoaFisica", PessoaFisica.class);

				if (data.isPresent()) {
					pf = data.get();
					loadPF(pf);
				}

				sync.asyncExec(() -> actionStatusBar.setLeftMessage(m.empty));
				sync.asyncExec(() -> fire(SEARCHING, false));
				sync.asyncExec(() -> fire(IS_LOADED, true));

				return Status.OK_STATUS;
			} catch (Exception e) {
				e.printStackTrace();
				sync.asyncExec(() -> fire(SEARCHING, false));
				return Status.CANCEL_STATUS;
			}
		});

		jobChecking.schedule();
	}

	public void searchAndLoadPJ(String scnpj) {
		this.jobChecking = new Job(m.textCheckingForPeople) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					sync.asyncExec(() -> fire(SEARCHING, true));
					sync.asyncExec(() -> actionStatusBar.setLeftMessage(IconType.WAIT, m.textCheckingForPeople,
							m.textPeopleJuridica));

					String query = "{pessoaJuridica(scnpj:\"%s\"){%s}}";

					CpfCnpj cc = new CpfCnpj(scnpj);
					Optional<PessoaJuridica> data = graphQL.query(query, cc.getNumber(), PJ_QUERY_RESPONSE)
							.getData("pessoaJuridica", PessoaJuridica.class);

					PessoaJuridica pj = data.orElse(null);

					loadPJ(pj);

					sync.asyncExec(() -> fire(SEARCHING, false));
					sync.asyncExec(() -> fire(IS_LOADED, true));

//					sync.asyncExec(() -> fire(SEARCHING, false));
					sync.asyncExec(() -> actionStatusBar.setLeftMessage(m.empty));
				} catch (Exception e) {
					fire(IS_LOADED, false);
					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}
		};

		jobChecking.schedule();
	}

	public void loadPF(PessoaFisica pf) {
		this.pf = pf;

		if (pf == null) {
			checksumPF = "";
		} else {
			checksumPF = MD5Utils.getChecksum(pf);
		}

		this.pj = null;

		sync.asyncExec(() -> {
			fire(LOAD_PF, pf);

			String title = pf == null ? m.titleDefaultCadastroPessoa : "";
			if(title.isEmpty()) {
				title = pf.getNome();
				title = title==null?m.titleDefaultCadastroPessoa:pf.getNome();
			}

			title = title.toUpperCase();
			
			fire(SET_PART_TITLE, title);
			fire(ENABLE_DIRTY, false);

		});
	}

	public void loadPJ(PessoaJuridica pj) {
		fire(SET_TYPE, Pessoa.JURIDICA);

		this.pj = pj;
		this.pf = null;

		if (pj == null) {
			fire(ENABLE_BOND, false, null);
		} else {

			String suf = pj.getUf();
			UF uf = UF.valueOf(suf);

			fire(SET_UF, uf);

			if (pj.getCapitalSocial() != null) {
				Locale locale = new Locale("pt", "BR");
				NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
				pj.setScapitalSocial(numberFormat.format(pj.getCapitalSocial()));
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

			pj.setMapSocios(mapSocios);

			Vinculo v = pj.getVinculo();
			if (v != null) {
				String matricula = String.format("%03d-%d", v.getId(), getMod10(v.getId() + ""));
				v.setMatricula(matricula);
			}

			fire(ENABLE_BOND, v != null, v);
		}

		fire(LOAD_PJ, pj);
	}

	public void bond() {
		Job job = Job.create(m.textBondPj, monitor -> {

			try {
				String query = "mutation{criarVinculo(scc:\"%s\"){%s}}";
				Optional<PessoaJuridica> data = graphQL.query(query, pj.getCnpj(), PJ_QUERY_RESPONSE)
						.getData("criarVinculo", PessoaJuridica.class);
				loadPJ(data.get());
			} catch (Exception e) {

			}

			return Status.OK_STATUS;
		});

		job.schedule();
	}

	public void unbond(boolean confirm) {
		if (confirm) {
			String message = m.questionDesvincular;

			if (pj != null) {
				CpfCnpj cc = new CpfCnpj(pj.getCnpj());
				message = MessageFormat.format(message, cc.getCpfCnpj());
				fire(QUESTION_REMOVE_BOND, message);
			}
			return;
		}

		Job job = Job.create(m.textUnbondPj, monitor -> {

			sync.asyncExec(() -> fire(SEARCHING, true));

			String query = "mutation{removerVinculos(scc:\"%s\"){%s}}";
			Optional<PessoaJuridica> data = graphQL.query(query, pj.getCnpj(), PJ_QUERY_RESPONSE)
					.getData("removerVinculos", PessoaJuridica.class);
			if (data.isPresent()) {
				loadPJ(data.get());
			}

			sync.asyncExec(() -> fire(SEARCHING, false));

			return Status.OK_STATUS;
		});

		job.schedule();
	}

	public void save() {
		Job job = Job.create(m.textUnbondPj, monitor -> {
			sync.asyncExec(() -> fire(SEARCHING, true));
			sync.asyncExec(() -> actionStatusBar.setLeftMessage(IconType.WAIT, m.textCheckingForPeople,
					m.textPeopleJuridica));
			
			
			String query = "mutation{pessoaFisica(pf:%s){%s}}";
			Optional<PessoaFisica> data = graphQL.query(query, graphQL.toGraph(pf), PF_QUERY_RESPONSE)
					.getData("pessoaFisica", PessoaFisica.class);
			if (data.isPresent()) {
				loadPF(data.get());
				sync.asyncExec(() -> fire(SEARCHING, false));
				sync.asyncExec(() -> actionStatusBar.setLeftMessage(m.empty));
			}
//			fire(ENABLE_DIRTY, false);
		});

		job.schedule();

	}

//	public void saveTestPF() {
//		this.pf = new PessoaFisica();
//		pf.setCpf("03057532900");
//		pf.setNome("Marcelo Lopes da Silva");
//		pf.setNascimento(DateUtils.getDate2("18/01/1979"));
//
//		try {
//			String query = "mutation{pessoaFisica(pf:%s){}}";
//
//			Optional<PessoaFisica> dataPF = graphQL.query(query, graphQL.toGraph(pf)).getData("pessoaFisica",
//					PessoaFisica.class);
//			if (dataPF.isPresent()) {
//				fire(LOAD_PF, dataPF.get());
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void changePF() {
		String newChecksum = MD5Utils.getChecksum(pf);
		fire(ENABLE_DIRTY, !checksumPF.equals(newChecksum));
	}

//	private boolean isChangePF(PessoaFisica pf) {
//		String md5 = toJson(pf);
//		md5 = MD5Utils.getHashMD5(md5);
//		return md5.equals(checksumPF);
//	}

	public void verifyPF() {
		loadPF(pf);
	}

	public void enterDataManually() {
		fire(ENABLE_MANUALLY_DATA, true);
	}

	public void cancelCheckingForPJ() {
		if (jobChecking != null) {
			jobChecking.cancel();
		}
		fire(SET_TYPE, Pessoa.NONE);
		loadPF(null);
		loadPJ(null);
//		fire(LOAD_PJ, (PessoaJuridica) null);
//		fire(LOAD_PF, (PessoaFisica) null);
		fire(IS_LOADED, false);
		fire(ENABLE_BOND, false);
		fire(RESET_PART);
	}

	public void writeIE(UF uf, String sie) {
		fireInCache(ENABLE_IE, uf != null);
		if (uf != null) {
			boolean ieValid = IEUtils.isIEValid(uf.getSigla(), sie);
			fireInCache(INFORME_VALID_IE, ieValid);
		}
	}

	public void writeCC(String scc) {
		CpfCnpj cc = new CpfCnpj(scc);
		if (cc.isValid()) {
			fireInCache(SET_TYPE, cc.isCnpj() ? Pessoa.JURIDICA : Pessoa.FISICA);
		} else {
			fireInCache(SET_TYPE, Pessoa.NONE);
		}
		fireInCache(INFORME_VALID_CC, cc.isValid());
	}

}
