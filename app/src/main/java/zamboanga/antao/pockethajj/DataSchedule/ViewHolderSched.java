package zamboanga.antao.pockethajj.DataSchedule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import zamboanga.antao.pockethajj.R;

/**
 * Created by abdulrahmanantao on 02/09/2017.
 */

public class ViewHolderSched extends RecyclerView.ViewHolder{

    TextView title;
    TextView content;
    TextView date;
    TextView time;

    ViewHolderSched(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.user_SchedTitle);
        content =(TextView) itemView.findViewById(R.id.user_SchedContent);
        date = (TextView) itemView.findViewById(R.id.user_SchedDate);
        time = (TextView) itemView.findViewById(R.id.user_SchedTime);

    }
}
