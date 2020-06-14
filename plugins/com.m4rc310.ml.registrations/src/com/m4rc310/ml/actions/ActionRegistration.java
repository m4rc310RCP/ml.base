package com.m4rc310.ml.actions;

import javax.inject.Inject;

import org.brazilutils.br.cpfcnpj.CpfCnpj;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;

import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar.IconType;
import com.m4rc310.ml.ui.actions.MAction;

@Creatable
//@Singleton
public class ActionRegistration extends MAction {

	public static final String LOAD_QRCODE_CLERK_REGISTRATION = "load_qrcode_clerk";
	public static final String INFORME_DATA_OK = "informe_data_ok";
	public static final String INFORME_VALID_ID = "informe_valid_id";
	public static final String LOAD_CLERK = "load_clerk";
	public static final String PREPARE_DIALOG = "prepare_dialog";
	public static final String  ON_FOCUS = " on_focus";
	public static final String  CHANGE_TITLE = "change_title";

	public enum EnumDialogState {
		PREPARE_TO_LOAD_CLERK, RESET_DIALOG, NO_CHANGE_CPF, CHANGE_CPF, CPF_VALID, CPF_INVALID, NO_HAS_CPF, HAS_CPF
	}

	@Inject
	private ActionStatusBar status;

	@Inject
	@Translation
	M m;

	public void readClerkName(String name) {
		boolean enabled = !name.isEmpty();
		fireInCache(INFORME_DATA_OK, enabled);
	}

	public void generateBarcodeClerk(String name) {
		try {
//			BufferedImage qrCode = BarcodeUtils.getQRCode(name, 300, 300);
//			fire(LOAD_QRCODE_CLERK_REGISTRATION, qrCode);
//			status.setLeftMessage(IconType.EMOTICON_FACE_GLASSES, m.textRegisterClerkInstruction);
		} catch (Exception e) {
			status.setLeftMessage(IconType.EMOTICON_FACE_SURPRISE, e.getMessage());
		}
	}
	
	public void onFocus() {
		fire(ON_FOCUS);
	}
	
	public void init() {
		onFocus();
		writeNome("");
	}

	public void newClerk() {

//		Clerk clerk = new Clerk();
//		clerk.setId(1L);
//		loadClerk(clerk);
	}

	public void noHasCpf(boolean has) {
		fire(PREPARE_DIALOG, has ? EnumDialogState.NO_HAS_CPF : EnumDialogState.HAS_CPF);
	}

	public void writeNome(String nome) {
		String defaultTitle = m.titleRegistrationEmployee;
		fireInCache(CHANGE_TITLE, nome.isEmpty()?defaultTitle:nome);
	}
	
	public void writeId(String sid) {
		boolean valid = sid.isEmpty();
		fireInCache(INFORME_VALID_ID, !valid);
	}

	public void writeCpf(String scpf) {
		CpfCnpj cpf = new CpfCnpj(scpf);
		fire(PREPARE_DIALOG, cpf.isValid() ? EnumDialogState.CPF_VALID : EnumDialogState.CPF_INVALID);
	}

	public void cancel() {
		fire(PREPARE_DIALOG, EnumDialogState.RESET_DIALOG);
//		loadClerk(null);
	}

	public void changeCPF() {
		fire(PREPARE_DIALOG, EnumDialogState.CHANGE_CPF);
		fire(PREPARE_DIALOG, EnumDialogState.CPF_VALID);
	}

//	public void clerkFronId(String sid) {
//		Long id = Long.parseLong(sid);
//		if (id == 1) {
//			fire(PREPARE_DIALOG, EnumDialogState.PREPARE_TO_LOAD_CLERK);
//			Pessoa clerk = new Pessoa();
//			clerk.setId(id);
//			clerk.setName("Marcelo Lopes da Silva");
//			clerk.setCpf("03057532900");
//			clerk.setNoContainCpf(true);
//
//			loadClerk(clerk);
//		} else {
//			fire(PREPARE_DIALOG, EnumDialogState.RESET_DIALOG);
//			loadClerk(null);
//		}
//	}

//	private void loadClerk(Pessoa clerk) {
//		fire(LOAD_CLERK, clerk);
//		if (clerk != null) {
//			fire(PREPARE_DIALOG, clerk.getCpf() != null ? EnumDialogState.NO_CHANGE_CPF : "");
//		}
//	}

	public void closeDialog() {
		status.setLeftMessage(IconType.EMOTICON_FACE_GRIN, "");
		writeId("");
	}

}
