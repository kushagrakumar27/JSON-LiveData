package com.example.hackernews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hackernews.Model.Article;
import com.example.hackernews.Model.ArticleResponse;
import com.example.hackernews.REST.ApiClient;
import com.example.hackernews.REST.NetworkCall;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Firebase mRef;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    ArrayList<Article> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


//        NewsAdapter newsAdapter = new NewsAdapter(articleList);

        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(newsAdapter);

        Call<List<Integer>> call = ApiClient.REST_CLIENT.getTopStories();
        call.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                List<Integer> topStories = response.body();
                for (int i = 0; i < topStories.size(); i++) {
                    ApiClient.REST_CLIENT.getArticle(topStories.get(i)).enqueue(new Callback<ArticleResponse>() {
                        @Override
                        public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
//                            String url = response.body().getUrl().toString();
                            if (response.body().getUrl() == null) {
                                response.body().setUrl("https://en.wikipedia.org/wiki/HTTP_404");
                            }
                            String title = response.body().getTitle().toString();
                            String url = response.body().getUrl().toString();

                            articleList.add(new Article(title, url));
                            NewsAdapter newsAdapter = new NewsAdapter(articleList);
                            recyclerView.setAdapter(newsAdapter);

                        }

                        @Override
                        public void onFailure(Call<ArticleResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {

            }
        });
//        Firebase.setAndroidContext();
    }

    /**
     * News Adapter
     */
    public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

        private ArrayList<Article> list;

        public NewsAdapter(ArrayList<Article> inputList) {
            this.list = inputList;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_row, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {
            holder.newsTitle.setText(list.get(position).Title);
            holder.newsTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = list.get(position).Url;   //final?
                    WebView wv = new WebView(getParent());
                    wv.loadUrl(url);
                    AlertDialog.Builder alert = new AlertDialog.Builder(getParent());
                    wv.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    alert.setView(wv);
                    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
//                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                    lp.copyFrom(dialog.getWindow().getAttributes());
//                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                    lp.gravity = Gravity.CENTER;
//                    dialog.getWindow().setAttributes(lp);
//                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                    LinearLayout parent = (LinearLayout) positiveButton.getParent();
//                    parent.setGravity(Gravity.CENTER_HORIZONTAL);
//                    View leftSpacer = parent.getChildAt(1);
//                    leftSpacer.setVisibility(View.GONE);
                }

            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitle;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.text_view);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String url = list.get(position).Url;
//                    WebView wv = new WebView(context);
//                    wv.loadUrl(url);
//                    AlertDialog.Builder alert = new AlertDialog.Builder(getParent());
//                    wv.setWebViewClient(new WebViewClient() {
//                        @Override
//                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                            view.loadUrl(url);
//                            return true;
//                        }
//                    });
//                    alert.setView(wv);
//                    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                        }
//                    });
//                    AlertDialog dialog = alert.create();
//                    dialog.show();
//                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                    lp.copyFrom(dialog.getWindow().getAttributes());
//                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//                    lp.gravity = Gravity.CENTER;
//                    dialog.getWindow().setAttributes(lp);
//                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                    LinearLayout parent = (LinearLayout) positiveButton.getParent();
//                    parent.setGravity(Gravity.CENTER_HORIZONTAL);
//                    View leftSpacer = parent.getChildAt(1);
//                    leftSpacer.setVisibility(View.GONE);
//                }
//            });
        }


    }
}

