package tj.test.chestersql;

import static tj.test.chestersql.Utils.CHILD_TYPE;
import static tj.test.chestersql.Utils.PARENT_TYPE;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataAdapter.OnItemClickListener {
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    DatabaseHelper dbhelper = new DatabaseHelper(this);

    private List<Tt> mItems = new ArrayList<>();
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mAdapter = new DataAdapter(this, mItems, this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mProgressBar = findViewById(R.id.progressBar);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mLayoutManager.findLastCompletelyVisibleItemPosition() == mItems.size() - 1) {
                    loadNextPage();
                }
            }
        });

        try {
            dbhelper.createDatabase();
            dbhelper.openDatabase();
            List<Tt> ttList = dbhelper.readDatabase(count, count + 20, PARENT_TYPE, 0);
            mItems = ttList;
            mAdapter.addItems(ttList);
            mAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNextPage() {
        mProgressBar.setVisibility(View.VISIBLE);

        int start = count + 20;
        int end = count + 40;

        List<Tt> ttList = dbhelper.readDatabase(start, end, PARENT_TYPE, 0);

        if (ttList.size() > 0) {
            mAdapter.addItems(ttList);
            count += 20;
        }

        mAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override

    public void onItemClick(Tt item) {
        count = 0;
        List<Tt> ttList = dbhelper.readDatabase(count, count + 20, CHILD_TYPE, item.getId());
        mAdapter.clear();
        mAdapter.addItems(ttList);
        mAdapter.notifyDataSetChanged();
    }
}