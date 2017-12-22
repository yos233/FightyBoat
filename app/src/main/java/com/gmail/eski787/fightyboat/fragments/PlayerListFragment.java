package com.gmail.eski787.fightyboat.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.eski787.fightyboat.R;
import com.gmail.eski787.fightyboat.databinding.LayoutPlayerShortBinding;
import com.gmail.eski787.fightyboat.game.Player;
import com.gmail.eski787.fightyboat.models.PlayerModel;

import java.util.List;

/**
 * This Fragment gets the list of Players from the {@link com.gmail.eski787.fightyboat.activities.NewGameActivity},
 * and displays it to the user in a {@link RecyclerView}.
 * Created by Erik on 11/24/2017.
 */

public class PlayerListFragment extends Fragment {
    private static String TAG = PlayerListFragment.class.getSimpleName();

    private List<Player> players;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PlayerListInteraction mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlayerListInteraction) {
            mListener = (PlayerListInteraction) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PlayerListInteraction");
        }
        players = mListener.getPlayerList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_player_list, container, false);

        mRecyclerView = view.findViewById(R.id.player_list);
        // Changes in content do not change the layout size.
        mRecyclerView.setHasFixedSize(true);
        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Setup Adapter
        mAdapter = new PlayerListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.player_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_play_game:
                Log.d(TAG, "Play Game");
                mListener.onStartGame();
                break;
            default:
                Log.d(TAG, "Received Unknown Option.");
                return false;
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        players = null;
    }

    /**
     * This interface must be implemented by activities that contain this fragment. This interface
     * allows for communication from the fragment back to the containing activity.
     */
    public interface PlayerListInteraction {
        /**
         * Gets the master list of players in the game.
         *
         * @return The list of Players.
         */
        List<Player> getPlayerList();

        /**
         * Called when the user selects a Player from the list.
         *
         * @param player   The Player that was selected.
         * @param itemView The list element View that was selected.
         */
        void onPlayerSelect(Player player, View itemView);

        /**
         * Called when the user commands the game to start.
         */
        void onStartGame();
    }

    private static class PlayerViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = PlayerViewHolder.class.getSimpleName();
        LayoutPlayerShortBinding binding;

        public PlayerViewHolder(View itemView) {
            super(itemView);
        }

        public PlayerViewHolder(LayoutPlayerShortBinding playerListBinding) {
            this(playerListBinding.getRoot());
            binding = playerListBinding;
        }
    }

    private class PlayerListAdapter extends RecyclerView.Adapter<PlayerViewHolder> {
        @Override
        public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = R.layout.layout_player_short;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            LayoutPlayerShortBinding playerShortBinding = DataBindingUtil
                    .inflate(inflater, layoutId, parent, false);
            return new PlayerViewHolder(playerShortBinding);
        }

        @Override
        public void onBindViewHolder(final PlayerViewHolder holder, int position) {
            final Player player = players.get(position);
            final PlayerModel model = new PlayerModel(player);
            holder.binding.setUser(model);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.d(TAG, "OnClick: " + player.getName());
                    mListener.onPlayerSelect(player, holder.itemView);
                }
            });
        }

        @Override
        public int getItemCount() {
            return players.size();
        }
    }
}
