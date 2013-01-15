package cz.vutbr.fit.testmind.fragments;

import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl.OnNodeSelectedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class NodeViewFragment extends Fragment implements OnNodeSelectedListener {
	
	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		
		return inflater.inflate(R.layout.fragment_node_view, container, false);
	}

	public void onNodeSelected(String htmlBody) {	
		
		if(webView == null) {
			webView = (WebView)getActivity().findViewById(R.id.fragment_node_view_body);
		}
		
		webView.loadData(htmlBody, "text/html; charset=UTF-8", null);
	}
}
