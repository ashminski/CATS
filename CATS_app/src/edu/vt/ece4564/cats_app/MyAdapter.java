package edu.vt.ece4564.cats_app;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * This class is the adapter for the custom rows in the listview
 * @author Ashley
 *
 */
public class MyAdapter extends BaseAdapter {

	private Context context;
	private List<Post> posts;
	
	public MyAdapter(Context context, List<Post> posts) {
		this.context = context;
		this.posts = posts;
	}

	@Override
	public int getCount() {
		return posts.size();
	}

	@Override
	public Object getItem(int position) {
		return posts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Post p = posts.get(position);
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_row, null);
        }

		TextView text = (TextView) convertView.findViewById(R.id.postText);
        text.setText(p.getText());

        TextView author = (TextView) convertView.findViewById(R.id.postedBy);
        author.setText("Posted by: " + p.getPostedBy() + " on " + p.getDatePosted());

        TextView location = (TextView) convertView.findViewById(R.id.gpsInfo);
        location.setText("Posted at: " + p.getCoordinates());
       

        return convertView;
	}

}
