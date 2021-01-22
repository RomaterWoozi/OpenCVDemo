package com.wuzx.atest;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.wuzx.atest.adapters.BrowseMediaItemsAdapter;
import com.wuzx.atest.adapters.SearchMediaItemsAdapter;


public class MediaAppControllerActivity extends AppCompatActivity {
    private static final String TAG = MediaAppControllerActivity.class.getSimpleName();
    private MediaControllerCompat mController;
    private MediaBrowserCompat mBrowser;
    private MediaAppDetails mMediaAppDetails;
    private MediaControllerCompat.Callback mCallback;
    private RatingUiHelper mRattingUiHelper;

    private BrowseMediaItemsAdapter mBrowseMediaItemsAdapter = new BrowseMediaItemsAdapter();
    private SearchMediaItemsAdapter mSearchMediaItemsAdapter = new SearchMediaItemsAdapter();
    private ViewGroup mRatingViewGroup;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_app_controller);

        mViewPager = findViewById(R.id.view_pager);

        final RecyclerView searchItemsList = findViewById(R.id.search_items_list);
        searchItemsList.setLayoutManager(new LinearLayoutManager(this));
        searchItemsList.setHasFixedSize(true);
        searchItemsList.setAdapter(mSearchMediaItemsAdapter);
        mSearchMediaItemsAdapter.init();
    }

    private void setupMediaController() {
        try {
            MediaSessionCompat.Token token = mMediaAppDetails.sessionToken;
            if (token == null) {
                token = mBrowser.getSessionToken();
            }
            mController = new MediaControllerCompat(this, token);
            mController.registerCallback(mCallback);
            mRattingUiHelper = ratingUiHelperFor(mController.getRatingType());

            mCallback.onPlaybackStateChanged(mController.getPlaybackState());
            mCallback.onMetadataChanged(mController.getMetadata());

            mViewPager.setVisibility(View.VISIBLE);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void setMediaBrowser() {
        mBrowser = new MediaBrowserCompat(this,
                mMediaAppDetails.componentName,
                new MediaBrowserCompat.ConnectionCallback() {
                    @Override
                    public void onConnected() {
                        setupMediaController();
                        mBrowseMediaItemsAdapter.setRoot(mBrowser.getRoot());
                    }

                    @Override
                    public void onConnectionSuspended() {
                        mBrowseMediaItemsAdapter.setRoot(null);
                    }

                    @Override
                    public void onConnectionFailed() {
                        super.onConnectionFailed();
                    }
                }, null);
        mBrowser.connect();
    }

    private RatingUiHelper ratingUiHelperFor(int ratingType) {
        switch (ratingType) {
            case RatingCompat.RATING_3_STARS:
                return new RatingUiHelper.Stars3(mRatingViewGroup, mController);
            case RatingCompat.RATING_4_STARS:
                return new RatingUiHelper.Stars4(mRatingViewGroup, mController);
            case RatingCompat.RATING_5_STARS:
                return new RatingUiHelper.Stars5(mRatingViewGroup, mController);
            case RatingCompat.RATING_HEART:
                return new RatingUiHelper.Heart(mRatingViewGroup, mController);
            case RatingCompat.RATING_THUMB_UP_DOWN:
                return new RatingUiHelper.Thumbs(mRatingViewGroup, mController);
            case RatingCompat.RATING_PERCENTAGE:
                return new RatingUiHelper.Percentage(mRatingViewGroup, mController);
            case RatingCompat.RATING_NONE:
            default:
                return new RatingUiHelper.None(mRatingViewGroup, mController);
        }
    }


}
