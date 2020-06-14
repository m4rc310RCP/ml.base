package com.m4rc310.ml.base.actions;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;

import com.m4rc310.ml.base.models.Data;
import com.m4rc310.rcp.graphql.MGraphQL;
import com.m4rc310.rcp.ui.utils.MAction;
import com.m4rc310.rcp.ui.utils.streaming.MEvent;

@Creatable
public class ActionCadastroContaML extends MAction {
	public static final String ABILITAR_BOTAO_LIBERAR = "abilitar_botao_liberar";
	public static final String AGUARDE_LIBERACAO = "aguarde_liberacao";
	public static final String ABILITAR_BOTAO_AUTORIAR_ML = "abilitar_botao_autoriar_ml";
	public static final String INFORME_ERRO = "informe_erro";
	public static final String CONTROLE_AMOSTRA = "controle_amostra";
	public static final String CANCELAR = "cancelar_processo";

	public static final int LIBERAR = 0;
	public static final int AUTORIZAR = 1;
	public static final int AGUARDAR = 2;
	public static final int OK = 3;

	@Inject
	private UISynchronize sync;
	
	@Inject MGraphQL graphql;
	
	
	private String codigo;
	
//	@Inject ML ml;

	public void init() {
		fire(ABILITAR_BOTAO_LIBERAR, false);
		fire(ABILITAR_BOTAO_AUTORIAR_ML, false);
		fire(INFORME_ERRO, "");
		fire(CONTROLE_AMOSTRA, LIBERAR);
	}

	public void digitaCodigo(String codigo) {
		fireInCache(ABILITAR_BOTAO_LIBERAR, codigo.matches("\\w{4,8}"));
		this.codigo = codigo;
	}

	public void autorizar() {
		sync.syncExec(() -> {
			fire(CONTROLE_AMOSTRA, AGUARDAR);
			sync.asyncExec(() -> {
				try {
					
					String query = "mutation{solicitarAcesso(email:\"%s\" sserial:\"%s\"){id diasAcesso dataPrimeiroAcesso}}";
					query = String.format(query, "marcelo.utfpr@me.com","1111");
					
					
					Data resp = graphql.query(query, Data.class);
					
					System.out.println(resp.acesso.getDataPrimeiroAcesso().getTime());
					
//					System.out.println(graphql.query(query));
					
					
//					DefaultApi api = ml.getApi();
//					String accessToken = api.getAuthUrl("https://m4rc310.herokuapp.com", AuthUrls.MLB);
//					System.out.println(accessToken);
					
					Thread.sleep(3000);
					fire(CONTROLE_AMOSTRA, OK);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});
	}
	
	public void cancelar() {
		init();
		fire(CONTROLE_AMOSTRA, LIBERAR);
		fire(CANCELAR);
	}
	

	public void solicitarLiberacao() {
		sync.syncExec(() -> {
			fire(AGUARDE_LIBERACAO, true);
			fire(INFORME_ERRO, "Aguarde liberação...");

			sync.asyncExec(() -> {
				try {
					System.out.println(codigo);

					Thread.sleep(2000);

					fire(INFORME_ERRO, "Sistema liberado com sucesso!");
					fire(ABILITAR_BOTAO_AUTORIAR_ML, true);
					fire(CONTROLE_AMOSTRA, AUTORIZAR);
				} catch (Exception e) {
					fire(INFORME_ERRO, e.getMessage());
					fire(AGUARDE_LIBERACAO, false);
					fire(ABILITAR_BOTAO_AUTORIAR_ML, false);
					fire(CONTROLE_AMOSTRA, LIBERAR);
					e.printStackTrace();
				}
			});

		});

	}

	@Override
	public void fire(String ref, Object... args) {
//		sync.syncExec(() -> {
		stream.fireListener(MEvent.event(this, ref, args));
//		});
	}

}
