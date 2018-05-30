package me.jfenn.feedage.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import me.jfenn.feedage.R;
import me.jfenn.feedage.adapters.ItemAdapter;
import me.jfenn.feedage.data.items.FeedItemData;
import me.jfenn.feedage.data.items.ItemData;
import me.jfenn.feedage.lib.FeedageLib;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;

public class FeedsFragment extends BasePagerFragment implements FeedageLib.OnCategoriesUpdatedListener {

    private RecyclerView recycler;
    private View refresh;
    private ImageView loading;
    private boolean shouldSwap;

    private AnimatedVectorDrawableCompat loadingDrawable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);
        recycler = v.findViewById(R.id.recycler);
        refresh = v.findViewById(R.id.refresh);
        loading = v.findViewById(R.id.loading);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        onFeedsUpdated(getFeedage().getFeeds());

        loadingDrawable = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.ic_feed_loading);
        loading.setImageDrawable(loadingDrawable);

        loadingDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                loading.post(() -> loadingDrawable.start());
            }
        });
        loadingDrawable.start();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFeedage().addListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFeedage().removeListener(this);
    }

    @Override
    public void onFeedsUpdated(List<FeedData> feeds) {
        if (feeds.size() > 0)
            loading.setVisibility(View.GONE);

        List<ItemData> items = new ArrayList<>();
        for (FeedData feed : feeds)
            items.add(new FeedItemData(feed));

        if (recycler.getAdapter() != null && recycler.getAdapter().getItemCount() > 0) {
            if (shouldSwap) {
                shouldSwap = false;
                recycler.swapAdapter(new ItemAdapter(items), true);
                refresh.setOnClickListener(null);
                refresh.animate().alpha(0).start();
            } else {
                refresh.setOnClickListener(v -> {
                    shouldSwap = true;
                    onFeedsUpdated(feeds);
                });
                refresh.animate().alpha(1).start();
            }
        } else recycler.setAdapter(new ItemAdapter(items));
    }

    @Override
    public void onCategoriesUpdated(List<CategoryData> categories) {

    }

    @Override
    public String getTitle() {
        return "Feeds";
    }
}
