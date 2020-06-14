package com.m4rc310.ml.base.actions;

import java.text.MessageFormat;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.osgi.util.NLS;

import com.m4rc310.ml.base.i18n.M;
import com.m4rc310.ml.base.models.Acesso;
import com.m4rc310.ml.base.models.Usuario;
import com.m4rc310.ml.base.utils.HardwareInfo;
import com.m4rc310.rcp.graphql.MGraphQL2;
import com.m4rc310.rcp.ui.utils.CronUtils;
import com.m4rc310.rcp.ui.utils.MAction;

@Creatable
@Singleton
@SuppressWarnings("restriction")
public class ActionCadastroML extends MAction {

	public static final String ABRIR_DIALOG_CONECCAO = "abrir_dialog_coneccao";
	public static final String INFORME_MESSAGE = "informe_error";
	public static final String INFORME_CONECCAO_SUCESSO = "informe_coneccao_sucesso";
	public static final String HABILITAR_BOTAO_AVANCAR = "habilitar_botao_avancar";
	public static final String HABILITAR_BOTAO_SOLICITAR = "habilitar_botao_solicitar";
	public static final String HABILITAR_BOTAO_INTEGRAR = "habilitar_botao_integrar";
	public static final String VERIFICANDO_CODIGO_ACESSO = "verificando_codigo_acesso";
	public static final String PREPARAR_DIGITACAO_EMAIL = "preparar_digitacao_email";
	public static final String SOLICITANDO_PERIODO_AVALIACAO = "solicitando_periodo_avaliacao";
	public static final String LOAD_USUARIO = "load_usuario";

	public static final String KEY_SERIAL = "key.local.serial";

	public static final int MESSAGEM_ERRO = 0;
	public static final int MESSAGEM_INFO = 1;
	public static final int MESSAGEM_WAIT = 2;
	public static final int MESSAGEM_EXCLAMATION = 3;

	@Inject
	private UISynchronize sync;
//	@Inject
//	private MGraphQL graphql;
	@Inject
	private MGraphQL2 graphql;
	@Inject
	@Translation
	M m;

	@Inject
	@Preference(nodePath = "com.m4rc310.ml.base")
	IEclipsePreferences prefs;

//	@Inject
//	private SpringBootWebSocketClient webSocket;

	@Inject
	CronUtils cron;
	private String codigo;
	private String email;
	private String serial;

	public void testeConeccao() {

		String query = "{acesso(serie:\"%s\"){id expirado}}";

		Optional<Acesso> optional = graphql.query(query, "1111").getData("acesso", Acesso.class);

		Acesso acesso = optional.orElseThrow(() -> graphql.getError());

		System.out.println(acesso.isExpirado());

//		cron.cron("0 * * * * *", () -> {
//			sync.syncExec(() -> {
//				try {
//					System.out.println("try...");
//					String query = "{testeConeccao}";
//					Data res = graphql.query(query, Data.class);
//					fire(INFORME_CONECCAO_SUCESSO, res.testeConeccao.equals("OK"));
//					cron.shutdown();
//				} catch (Exception e) {
//					fire(INFORME_MESSAGE, MESSAGEM_ERRO, "Erro de Conecção");
//					fire(INFORME_CONECCAO_SUCESSO, false);
//				}
//			});
//
//		});
	}

	public void configure() {
		this.serial = HardwareInfo.getSerialNumber();

		try {
			String query = "{acesso(serie:\"%s\"){id expirado}}";
			query = String.format(query, serial);

			System.out.println(query);

//			Data resp = graphql.query(query, Data.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

//
//		if (serial.isEmpty()) {
//			try {
//
//				String serialDefault = new SecureRandom().ints(0, 36).mapToObj(i -> Integer.toString(i, 36))
//						.map(String::toUpperCase).distinct().limit(8).collect(Collectors.joining())
//						.replaceAll("([A-Z0-9]{4})", "$1-").substring(0, 9);
//
//				prefs.put(KEY_SERIAL, serialDefault);
//				prefs.flush();
//				this.serial = serialDefault;
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//			}
//		}
	}

	public void validarCodigo() {

		String query = "{acesso(serie:\"%s\"){id bloqueado dataFimAcesso usuario{email}}}";

		if (!codigo.isEmpty()) {
			fire(VERIFICANDO_CODIGO_ACESSO, true);
			fire(INFORME_MESSAGE, MESSAGEM_WAIT, m.textValidandoCodigoAcesso);
			sync.asyncExec(() -> {
				try {

					graphql.query(query, codigo);

					Acesso acesso = graphql.getData("acesso", Acesso.class).orElseThrow(() -> graphql.getError());

					fire(VERIFICANDO_CODIGO_ACESSO, false);
//					fire(INFORME_MESSAGE, MESSAGEM_INFO, m.textCodigoAcessoValido);

					String message = MessageFormat.format(m.textCodigoAcessoValido,
							acesso.getDataFimAcesso().getTime());
					fire(INFORME_MESSAGE, MESSAGEM_INFO, message);
					fire(LOAD_USUARIO, acesso.getUsuario());

				} catch (Exception e) {
					fire(VERIFICANDO_CODIGO_ACESSO, false);
					fire(INFORME_MESSAGE, MESSAGEM_ERRO, e.getMessage());
					fire(LOAD_USUARIO, (Object) null);
				}
			});
		}
	}

	public void digitandoEmail(String email) {
		String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		boolean valid = email.matches(regex);
		fireInCache(HABILITAR_BOTAO_SOLICITAR, valid);
		this.email = valid ? email : "";
	}

	public void digitandoCodigo(String codigo) {
		boolean ok = codigo.matches("\\w{4,8}");
		fireInCache(HABILITAR_BOTAO_AVANCAR, ok);
		this.codigo = ok ? codigo : "";
	}

	public void init() {
		fire(HABILITAR_BOTAO_AVANCAR, false);
		fire(INFORME_MESSAGE, MESSAGEM_EXCLAMATION, m.textIntegracaoInfo);
		fire(HABILITAR_BOTAO_SOLICITAR, false);
	}

	public void abrirDialogConnect() {
		fire(ABRIR_DIALOG_CONECCAO);
	}

	public void prepararSolicitacao() {
		fire(PREPARAR_DIGITACAO_EMAIL);
		fire(INFORME_MESSAGE, MESSAGEM_EXCLAMATION, m.textInformeSolicitacao);
	}

	public void solicitarPeriodoAvaliacao() {
		sync.syncExec(() -> {
			fire(SOLICITANDO_PERIODO_AVALIACAO, true);
			fire(INFORME_MESSAGE, MESSAGEM_WAIT, m.textSolicitandoAvaliacao);

			sync.asyncExec(() -> {
				try {
					String query = "mutation{solicitarAcessoTemporario(serie:\"%s\", email:\"%s\"){id email acesso{codigo  dataFimAcesso} }}";
					graphql.query(query, "1113", email);

					Usuario usuario = graphql.getData("solicitarAcessoTemporario", Usuario.class)
							.orElseThrow(() -> graphql.getError());

					String text = NLS.bind(m.textAutorizadoPeriodoTeste, usuario.getAcesso().getDataFimAcesso());

					fire(INFORME_MESSAGE, MESSAGEM_INFO, text);

				} catch (Exception e) {
					e.printStackTrace();
					fire(INFORME_MESSAGE, MESSAGEM_ERRO, e.getMessage());
				}
			});

		});

	}
}
