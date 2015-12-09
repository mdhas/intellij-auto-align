import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

public class CodeAlignerAction extends AnAction {

    private final Aligner aligner       = new Aligner(AlignerOptions.Default());

	private static final void log(String message, Object... args) {
		System.out.println(String.format(message, args));
	}

	@Override
	public void actionPerformed(AnActionEvent e) {
        Project project                 = e.getData(PlatformDataKeys.PROJECT);
        Editor editor                   = e.getData(PlatformDataKeys.EDITOR);

		if (editor == null) {
			return;
		}

		if (!editor.getDocument().isWritable()) {
			return;
		}

        Document document               = editor.getDocument();
        SelectionModel selection        = editor.getSelectionModel();
        String selectedText             = selection.getSelectedText();

		String autoAlignedText;
		int startOffset;
		int endOffset;

		if (selectedText != null) {
			// just align the selected text
	        autoAlignedText             = aligner.align(selectedText);
            startOffset                 = selection.getSelectionStart();
            endOffset                   = selection.getSelectionEnd();
		} else {
			// auto-align the whole document
			autoAlignedText            = aligner.align(document.getText());
            startOffset                 = 0;
            endOffset                   = document.getTextLength();
		}

		replaceString(project, document, autoAlignedText, startOffset, endOffset);
	}


	private void replaceString(Project project, Document document, String replaceText, int startOffset, int endOffset) {
		CommandProcessor.getInstance().executeCommand(project, (Runnable) () -> {
			ApplicationManager.getApplication().runWriteAction((Runnable) () -> {
				document.replaceString(startOffset, endOffset, replaceText);
			});
		}, "Auto-Align", this);
	}
}