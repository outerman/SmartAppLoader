package com.shenxy.smartapploader.wiget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenxy.smartapploader.R;
import com.shenxy.smartapploader.utils.DensityUtil;
import com.shenxy.smartapploader.utils.NameLengthFilter;

//import com.chanjet.libutil.util.Dip2PxUtil;
//import com.chanjet.libutil.util.NameLengthFilter;
//import com.chanjet.sharedRes.R;

/**
 * 实现了Dialog的布局，使用时需要设置title，内容区域的View,底部Button的Text，Button的OnClickListener
 *
 */
public class ChanjetDialog extends Dialog {

    // private static final String TAG = "CJDialog";
    private TextView mTitle;
    private DialogContentLayout mContent;
    private LinearLayout mBottomLayout;
    private TextView mButton1;
    private TextView mButton2;
    private TextView mButton3;
    private TextView mDefaultText;
    private Context context;

    private EditText editText;
    private TextView textView;

    private CheckBox checkBox;
    private TextView checkBoxHint;
    private RelativeLayout checkRayout;
    private int MAX_EDIT_LENGTH = 200;//文本输入框需限制字数。（需求：最多1024汉字，2048字符）

    public ChanjetDialog(Context context) {
        super(context, R.style.FullHeightDialog);
        this.context = context;
        dialogLayout(context);
    }

    /**
     * 设置Dialog的内容
     *
     * @param contentView
     */
    public void setContentView(View contentView) {
        if (mContent.getParent() != null) {
            mContent.removeAllViews();
        }
        mContent.addView(contentView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * @param text
     */
    public void setDialogText(CharSequence text) {
        if (mDefaultText == null) {
            final Resources res = context.getResources();
            final int p_horizontal = res.getDimensionPixelSize(R.dimen.dialog_content_padding_horizontal);
            final int p_vertical = res.getDimensionPixelSize(R.dimen.dialog_content_padding_vertical);
            mDefaultText = new TextView(context);
            mDefaultText.setGravity(Gravity.CENTER);
            mDefaultText.setText(text);
            mDefaultText.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimensionPixelSize(R.dimen.dialog_text_size));
            mDefaultText.setTextColor(res.getColor(R.color.dialog_default_text));
            mDefaultText.setPadding(p_horizontal, p_vertical, p_horizontal, p_vertical);
        }
        setContentView(mDefaultText);
    }


    public void setEditTextVisible(String hint){
        editText.setVisibility(View.VISIBLE);
        if (hint != null && !"".equals(hint)){
            editText.setHint(hint);
        }
        mContent.setVisibility(View.GONE);
    }

    public void setEditTextFocus(){
        if (editText != null && editText.isShown()) {
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public String getEditTextContent(){
        if (editText != null && editText.isShown()){
            return editText.getText().toString();
        }
        return "";
    }

    public void setEditTextContent(String text){
        if (editText != null){
            editText.setText(text);
        }
    }

    public void setTextViewVisible(String text,boolean isCenter){
        textView.setVisibility(View.VISIBLE);
        if (text != null && !"".equals(text)){
            textView.setText(text);
        }
        if(isCenter){//是否居中显示
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        mContent.setVisibility(View.GONE);
    }

    /**
     * 设置Dialog的title
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    /**
     * 设置bottom layout是否可见
     *
     * @param visible
     */
    public void setBottomVisible(boolean visible) {
        if (visible) {
            mBottomLayout.setVisibility(View.VISIBLE);
        } else {
            mBottomLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 设置title不显示
     */
    public void setTitleVisible(boolean visible) {
        if (visible) {
            mTitle.setVisibility(View.VISIBLE);
        } else {
            mTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置底部Button
     *
     * @param botton1_title
     * @param listener
     */
    public void setButton(CharSequence botton1_title,
                          View.OnClickListener listener) {
        mButton1.setText(botton1_title);
        mButton1.setOnClickListener(listener);

        mBottomLayout.setVisibility(View.VISIBLE);
        mButton1.setVisibility(View.VISIBLE);
        mButton1.setBackgroundResource(R.drawable.chanjet_dialog_button_single_selector);
    }

    /**
     * 设置底部Button
     *
     * @param botton1_title
     * @param listener1
     * @param botton2_title
     * @param listener2
     */
    public void setButton(CharSequence botton1_title,
                          View.OnClickListener listener1, CharSequence botton2_title,
                          View.OnClickListener listener2) {
        mButton1.setText(botton1_title);
        mButton2.setText(botton2_title);

        mButton1.setOnClickListener(listener1);
        mButton2.setOnClickListener(listener2);

        mBottomLayout.setVisibility(View.VISIBLE);
        mButton1.setVisibility(View.VISIBLE);
        mButton2.setVisibility(View.VISIBLE);
        mButton1.setBackgroundResource(R.drawable.chanjet_dialog_button_left_selector);
        mButton2.setBackgroundResource(R.drawable.chanjet_dialog_button_right_selector);
    }

    /**
     * 设置底部Button
     *
     * @param botton1_title
     * @param listener1
     * @param botton2_title
     * @param listener2
     * @param botton3_title
     * @param listener3
     */
    public void setButton(CharSequence botton1_title,
                          View.OnClickListener listener1, CharSequence botton2_title,
                          View.OnClickListener listener2, CharSequence botton3_title,
                          View.OnClickListener listener3) {
        mButton1.setText(botton1_title);
        mButton2.setText(botton2_title);
        mButton3.setText(botton3_title);

        mButton1.setOnClickListener(listener1);
        mButton2.setOnClickListener(listener2);
        mButton3.setOnClickListener(listener3);

        mBottomLayout.setVisibility(View.VISIBLE);
        mButton1.setVisibility(View.VISIBLE);
        mButton2.setVisibility(View.VISIBLE);
        mButton3.setVisibility(View.VISIBLE);
        mButton1.setBackgroundResource(R.drawable.chanjet_dialog_button_left_selector);
        mButton2.setBackgroundResource(R.drawable.chanjet_dialog_button_single_selector);
        mButton3.setBackgroundResource(R.drawable.chanjet_dialog_button_right_selector);
    }
    public  void showCheckBox(String checkboxTitle,View.OnClickListener checkboxOnClickListener){
        checkRayout.setVisibility(View.VISIBLE);
        checkBoxHint.setText(checkboxTitle);
        checkBox.setGirdChecked(false);
        checkBox.setOnClickListener(checkboxOnClickListener);
        mContent.setVisibility(View.GONE);
    }
    /**
     * Dialog布局
     */
    private void dialogLayout(Context context) {

        // root
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LinearLayout root = (LinearLayout) View.inflate(context,
                R.layout.dialog_layout, null);
        setContentView(root, params);
//        Dialog dialog=new Dialog(context);
//        dialog.getWindow().setLayout(700, 500);
        WindowManager.LayoutParams params1 = getWindow().getAttributes();  
        params1.horizontalMargin= DensityUtil.dip2px(context, 15);
        params1.width=WindowManager.LayoutParams.FILL_PARENT;
        params1.height=WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params1);  
        
        mTitle = (TextView) root.findViewById(R.id.dialog_title);
        mBottomLayout = (LinearLayout) root.findViewById(R.id.dialog_bottom);
        mContent = (DialogContentLayout) root.findViewById(R.id.dialog_content);

        editText = (EditText) root.findViewById(R.id.dialog_edit);
        editText.setFilters(new InputFilter[]{new NameLengthFilter(MAX_EDIT_LENGTH)});
        textView = (TextView) root.findViewById(R.id.dialog_text);;

        mButton1 = (TextView) root.findViewById(R.id.dialog_button1);
        mButton2 = (TextView) root.findViewById(R.id.dialog_button2);
        mButton3 = (TextView) root.findViewById(R.id.dialog_button3);
        final int textSize = context.getResources().getDimensionPixelSize(R.dimen.dialog_text_size);
        final int textSizeButton = context.getResources().getDimensionPixelSize(R.dimen.dialog_text_size_button);
        mButton1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeButton);
        mButton2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeButton);
        mButton3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeButton);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        mBottomLayout.setVisibility(View.GONE);
        mButton1.setVisibility(View.GONE);
        mButton2.setVisibility(View.GONE);
        mButton3.setVisibility(View.GONE);

        checkBox= (CheckBox) findViewById(R.id.checkBox);
        checkBoxHint= (TextView) findViewById(R.id.txtHint);
        checkRayout= (RelativeLayout) findViewById(R.id.rayoutCheckBox);
        checkRayout.setVisibility(View.GONE);
    }

    public void setButton1TextColor(int color) {
		mButton1.setTextColor(color);
	}
    public void setButton2TextColor(int color) {
    	mButton2.setTextColor(color);
    }


    @Override
    public void show() {
        //如果所属的Activity已经在销毁，则不做显示
        if (context instanceof Activity && ((Activity)context).isFinishing()){
            return;
        }
        super.show();
    }


}
