package com.example.trackingmypantrygiacomochinilam.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.trackingmypantrygiacomochinilam.R;

public class ProfileFragment extends Fragment {

    private Button logout;
    private TextView id, user, email, created;

    private ProfileViewModel profileViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        id = root.findViewById(R.id.profileId);
        user = root.findViewById(R.id.profileUser);
        email = root.findViewById(R.id.profileEmail);
        created = root.findViewById(R.id.profileDate);
        logout = root.findViewById(R.id.logoutBtn);

        //view model
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        profileViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String res) {
                user.setText(res);
            }
        });
        profileViewModel.getEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String res) {
                email.setText(res);
            }
        });
        profileViewModel.getId().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String res) {
                id.setText(res);
            }
        });
        profileViewModel.getCreated().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String res) {
                created.setText(res);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileViewModel.onLogoutAction(getActivity());

                getActivity().finish();
                System.exit(0);
            }
        });

        return root;
    }
}