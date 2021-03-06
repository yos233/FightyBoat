package com.gmail.eski787.fightyboat.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.eski787.fightyboat.R;
import com.gmail.eski787.fightyboat.databinding.FragmentPlayerDetailBinding;
import com.gmail.eski787.fightyboat.game.Player;
import com.gmail.eski787.fightyboat.models.PlayerModel;

/**
 * This fragment will provide detailed information about a player, and allow them to change
 * their name, avatar image, and ship placement.
 * Created by Erik on 11/24/2017.
 */
public class PlayerDetailFragment extends PlayerFragment {
    private static String TAG = PlayerDetailFragment.class.getSimpleName();
    private PlayerDetailInteraction mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlayerDetailInteraction) {
            mListener = (PlayerDetailInteraction) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PlayerDetailInteraction.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final int layoutId = R.layout.fragment_player_detail;
        FragmentPlayerDetailBinding binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        binding.setUser(new PlayerModel(getPlayer()));

        // Add onClick callbacks
        binding.playerDetailChangeLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChangeLock(getPlayer());
            }
        });

        binding.playerDetailMoveShips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMoveShips(getPlayer());
            }
        });

        Toolbar toolbar = binding.playerDetailToolbar;
        toolbar.setTitle(R.string.player_detail_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return binding.getRoot();
    }

    public interface PlayerDetailInteraction extends PlayerFragmentInteraction {
        void onChangeLock(Player player);

        void onMoveShips(Player player);
    }
}
