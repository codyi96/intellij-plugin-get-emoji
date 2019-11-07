package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import emojis.V120;
import org.jetbrains.annotations.NotNull;

/**
 * Created by zou on 2019/11/7.
 *
 */
public class Emoji extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取项目对象
        Project project = e.getData(PlatformDataKeys.PROJECT);
        // 获取编辑器对象
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        // 获取光标对象 用于控制光标
        CaretModel caretModel = editor.getCaretModel();
        // 获取文档对象 用于操作文档
        Document document = editor.getDocument();
        // 使用ListPopupStep来展示可选Emoji
        BaseListPopupStep<String> baseListPopupStep
                = new BaseListPopupStep<String>("emoji", V120.emoji()) {
            @NotNull
            @Override
            public String getTextFor(String value) {
                return value;
            }

            @Override
            public PopupStep onChosen(String selectedValue, boolean finalChoice) {
                if (finalChoice) {
                    // 获取当前光标的偏移值
                    int curIndex = caretModel.getOffset();
                    System.out.println(curIndex);
                    System.out.println(selectedValue);
                    Runnable runnable = () -> {
                        // 插入Emoji并移动光标
                        document.insertString(curIndex, selectedValue);
                        caretModel.moveToOffset(curIndex + selectedValue.length());
                    };
                    // 文本操作需要放置在WriteCommandAction中交由后台处理
                    WriteCommandAction.runWriteCommandAction(project, runnable);
                }
                return super.onChosen(selectedValue, finalChoice);
            }

            @Override
            public boolean isSpeedSearchEnabled() {
                return false;
            }
        };
        // 创建ListPopup并展示
        ListPopup listPopup = JBPopupFactory
                .getInstance()
                .createListPopup(baseListPopupStep);
        listPopup.showInBestPositionFor(editor);
    }
}
