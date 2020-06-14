package com.m4rc310.ml.base.actions;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;

import com.m4rc310.ml.base.i18n.M;
import com.m4rc310.ml.base.models.Acesso;
import com.m4rc310.ml.base.models.Funcionario;
import com.m4rc310.ml.base.reports.Page;
import com.m4rc310.ml.base.utils.HardwareInfo;
import com.m4rc310.rcp.graphql.MGraphQL2;
import com.m4rc310.rcp.ui.utils.MAction;
import com.m4rc310.rcp.ui.utils.PartControl;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import reports.utils.R;

@Creatable
@Singleton
public class ActionML extends MAction {

	public static final String OPEN_DIALOG_INTEGRATION = "open_dialog_integration";

	public static final String SHOW_MESSAGE = "show_message";
	public static final String MESSAGE_INFORMATION = "icons/exclamation.png";
	public static final String MESSAGE_QUESTION = "show_message_question";
	public static final String MESSAGE_ERROR = "icons/exclamation-2.png";
	public static final String MESSAGE_WAIT = "icons/wait.png";
	public static final String MESSAGE_OK = "icons/ok.png";

	public static final String PRE_VALIDATE_CODE = "pre_validate_code";
	public static final String VALIDATING_CODE = "validating_code";

	public static final String TO_CHOOSE = "to_choose";

	public static final String SHOW_REPORT = "show_report";
	public static final String LOAD_REPORT_TEST = "load_report_test";

	public static final int INIT_ACTIONS = 0;
	public static final int VALIDATE_CODE = 1;
	public static final int GET_PERIOD_TESTS = 2;
	public static final int VALID_EMAIL = 3;
	public static final int VALID_CODE = 4;
	public static final int LOAD_ACESSO = 5;
	public static final int LOCK_DIALOG = 6;
	public static final int REGISTER = 7;

	public static final String INFORME_IzNIT_DIALOG = "informe_init_dialog";
	public static final String DISPOSE = "dispose";

	public static final String REQUESTING_AVALUATION_PERIOD = "requesting_avaluation_period";

	@Inject
	@Translation
	M m;

	@Inject
	PartControl pc;

	@Inject
	UISynchronize sync;

	@SuppressWarnings("unused")
	private String email;

	private String code;

	@Inject
	MGraphQL2 graphQL;

	public void openDialogInit() {
		fire(OPEN_DIALOG_INTEGRATION);
	}

	public void init() {
		sync.syncExec(() -> {
			try {
				String query = "{testConnection}";
				graphQL.query(query);
				graphQL.getData("testConnection", String.class).orElse("");
//				boolean connected = "OK".equalsIgnoreCase(resp);
				fire(SHOW_MESSAGE, MESSAGE_INFORMATION, m.textChooseAction);
				fire(TO_CHOOSE, INIT_ACTIONS);
			} catch (Exception e) {
				fire(SHOW_MESSAGE, MESSAGE_ERROR, m.textConnectedInvailable);
				fire(TO_CHOOSE, LOCK_DIALOG);
			}
		});
	}

	public void testReport() {
		fire(SHOW_REPORT);
		R.compileReports("com.m4rc310.ml.base", "sreports");

		sync.asyncExec(() -> {
			try {
				String query = "{funcionarios{matricula nomeCompleto cargo{nome} }}";
				List<Funcionario> funcionarios = graphQL.query(query).getDataList("funcionarios", Funcionario.class);

				Page page = new Page();
				page.setFuncionarios(funcionarios);
				
				
				JasperReport report = R.getReport("com.m4rc310.ml.base.test");
				JasperPrint print = R.getJasperPrint(report, null, Arrays.asList(page));
				fire(LOAD_REPORT_TEST, print);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	public void chooseOptionToInformationAccessCode() {
		fire(SHOW_MESSAGE, MESSAGE_INFORMATION, m.textIntegracaoInfo);
		fire(TO_CHOOSE, VALIDATE_CODE);
	}

	public void chooseGetTestPeriod() {
		try {
			fire(SHOW_MESSAGE, MESSAGE_INFORMATION, m.textSolicitarPeriodoAvaliacao);
			fire(TO_CHOOSE, GET_PERIOD_TESTS);

			String serialNumber = HardwareInfo.getSerialNumber();
			String query = "{acesso(serie:\"%s\"){id expirado}}";

			graphQL.query(query, code, serialNumber);

			Acesso acesso = graphQL.getData("acesso", Acesso.class).orElse(null);

			if (acesso == null) {
				acesso = new Acesso();
				acesso.setSerial(serialNumber);
				fire(TO_CHOOSE, LOAD_ACESSO, acesso);
				return;
			}

			if (acesso.isExpirado()) {
				throw new Exception(m.errorPeriodoTestesExpirado);
			}

		} catch (Exception e) {
			fire(SHOW_MESSAGE, MESSAGE_ERROR, e.getMessage());
		}

	}

	public void validatingCode() {
		sync.syncExec(() -> {
			fire(SHOW_MESSAGE, MESSAGE_WAIT, m.textValidandoCodigoAcesso);
			fire(VALIDATING_CODE, true);

			sync.asyncExec(() -> {
				try {

					String serialNumber = HardwareInfo.getSerialNumber();

					String query = "{validarAcesso(codigo:\"%s\" serie:\"%s\"){id dataFimAcesso}}";

					graphQL.query(query, code, serialNumber);

					Acesso acesso = graphQL.getData("validarAcesso", Acesso.class).orElse(null);

					if (acesso == null) {
						throw new Exception(m.errorInvalidCode);
					}

					fire(SHOW_MESSAGE, MESSAGE_OK,
							pc.toString(m.textCodigoAcessoValido, acesso.getDataFimAcesso().getTime()));

					sync.asyncExec(() -> {
						try {
							Thread.sleep(3000);
							fire(DISPOSE);
						} catch (Exception e) {
							// TODO: handle exception
						}
					});

				} catch (Exception e) {
					fire(SHOW_MESSAGE, MESSAGE_ERROR, e.getMessage());
					fire(VALIDATING_CODE, false);
					fire(TO_CHOOSE, VALID_CODE, false);
				}
			});

		});

	}

	public void requestAvaluationPeriod() {
		fire(SHOW_MESSAGE, MESSAGE_WAIT, m.textSolicitandoAvaliacao);
		fire(REQUESTING_AVALUATION_PERIOD, true);
	}

	public void writingEmail(String email) {
		String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		boolean valid = email.matches(regex);
		fireInCache(TO_CHOOSE, VALID_EMAIL, valid);
		this.email = valid ? email : "";
	}

	public void writingCode(String code) {
		boolean valid = code.matches("\\d{4,10}");
		fireInCache(PRE_VALIDATE_CODE, valid);
		this.code = valid ? code : "";
	}

}
