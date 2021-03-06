/*
 * Copyright (c) 2019 Cisco and/or its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cisco.hicn.forwarder.forwarder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cisco.hicn.forwarder.R;
import com.cisco.hicn.forwarder.service.BackendAndroidService;
import com.cisco.hicn.forwarder.service.BackendProxyService;
import com.cisco.hicn.forwarder.supportlibrary.Forwarder;
import com.cisco.hicn.forwarder.supportlibrary.HProxy;
import com.cisco.hicn.forwarder.utility.Constants;

public class ForwarderFragment extends Fragment {


    private TextView forwarderStatusTextView = null;
    private Switch forwarderSwitch = null;
    private static int VPN_REQUEST_CODE = 100;

    private SharedPreferences sharedPreferences;


    private OnFragmentInteractionListener mListener;

    public ForwarderFragment() {
    }

    public static ForwarderFragment newInstance(String param1, String param2) {
        ForwarderFragment fragment = new ForwarderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forwarder, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        forwarderStatusTextView = root.findViewById(R.id.forwarder_status_text_view);

        forwarderSwitch = root.findViewById(R.id.forwarder_switch);

        if (Forwarder.getInstance().isRunningForwarder()) {
            forwarderSwitch.setChecked(true);
            forwarderStatusTextView.setText(Constants.ENABLED);
        } else {
            forwarderSwitch.setChecked(false);
            forwarderStatusTextView.setText(Constants.DISABLED);
        }

        forwarderSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            Log.v("Switch State=", "" + isChecked);
            if (isChecked) {
                forwarderStatusTextView.setText(Constants.ENABLED);
                startBackend();
            } else {
                forwarderStatusTextView.setText(Constants.DISABLED);
                stopBackend();
            }
        });

        if (Forwarder.getInstance().isRunningForwarder()) {
            forwarderStatusTextView.setText(Constants.ENABLED);
            forwarderSwitch.setChecked(true);
        } else {
            forwarderStatusTextView.setText(Constants.DISABLED);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void startBackend() {
        Intent intent = new Intent(getActivity(), BackendAndroidService.class);
        getActivity().startService(intent);


        HProxy hproxy = HProxy.getInstance();


        if (hproxy.isHProxyEnabled()) {

            Intent intentProxy = null;

            intentProxy = BackendProxyService.prepare(getActivity());

            if (intentProxy != null) {
                startActivityForResult(intentProxy, VPN_REQUEST_CODE);
            } else {
                this.getActivity().startService(getServiceIntent().setAction(BackendProxyService.ACTION_CONNECT));
            }
        }
    }

    private void stopBackend() {
        Intent intent = new Intent(getActivity(), BackendAndroidService.class);
        getActivity().stopService(intent);
        HProxy hproxy = HProxy.getInstance();
        if (hproxy.isHProxyEnabled()) {
            this.getActivity().startService(getServiceIntent().setAction(BackendProxyService.ACTION_DISCONNECT));
        }
    }

    private Intent getServiceIntent() {
        return new Intent(getActivity(), BackendProxyService.class);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VPN_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            this.getActivity().startService(getServiceIntent().setAction(BackendProxyService.ACTION_CONNECT));

    }
}
