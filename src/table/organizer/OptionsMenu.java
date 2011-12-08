package table.organizer;

import table.organizer.model.TableManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

public class OptionsMenu {
	public static boolean optionsMenuItemPicker(MenuItem item, final Activity activity, BaseAdapter adapter) {
		Context context = (Context)activity;
		switch (item.getItemId()) {
        case R.id.clear:
        	showClearTableDialog(context, adapter);
        	return true;
        case R.id.tip:
        	activity.showDialog(Table.TIP_DIALOG);
        	return true;
        case R.id.help:
            return true;
        default:
			return false;
		}
	}
	
	private static void showClearTableDialog(final Context context, final BaseAdapter adapter) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.confirmClearTable)
		.setCancelable(false)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TableManager.getInstance(context).clear();				
				adapter.notifyDataSetChanged();
	        }
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		Log.d("tag", "mostrando confirmacao");
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static Dialog createNewTipDialog(final Context context, final BaseAdapter adapter) {
		final Dialog dialog = new Dialog(context);
			
		dialog.setContentView(R.layout.tip_dialog);
		dialog.setTitle(R.string.tipDialogTitle);
		
		final EditText tipValueText = (EditText) dialog.findViewById(R.id.tipValue);
		tipValueText.setText(""+TableManager.getInstance(context).getTip());
		
		Button button = (Button) dialog.findViewById(R.id.ok);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int tipValue = Integer.parseInt(tipValueText.getText().toString());
				TableManager.getInstance(context).setTip(tipValue);
				dialog.dismiss();
				adapter.notifyDataSetChanged();
			}
		});
		
		return dialog;
	}
}
