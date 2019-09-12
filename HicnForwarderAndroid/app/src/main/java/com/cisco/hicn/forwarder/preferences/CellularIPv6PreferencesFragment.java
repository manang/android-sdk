package com.cisco.hicn.forwarder.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.cisco.hicn.forwarder.R;
import com.cisco.hicn.forwarder.supportlibrary.NativeAccess;
import com.cisco.hicn.forwarder.utility.Constants;

import org.apache.http.conn.util.InetAddressUtilsHC4;

public class CellularIPv6PreferencesFragment extends PreferenceFragmentCompat {
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.cellular_ipv6, rootKey);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getPreferenceScreen().findPreference(getString(R.string.cellular_source_port_ipv6_key)).setEnabled(sharedPreferences.getBoolean(getString(R.string.enable_nexthop_ipv4_key), false));
        getPreferenceScreen().findPreference(getString(R.string.cellular_nexthop_ipv6_key)).setEnabled(sharedPreferences.getBoolean(getString(R.string.enable_nexthop_ipv4_key), false));
        getPreferenceScreen().findPreference(getString(R.string.cellular_nexthop_port_ipv6_key)).setEnabled(sharedPreferences.getBoolean(getString(R.string.enable_nexthop_ipv4_key), false));


        getPreferenceScreen().findPreference(getString(R.string.cellular_source_port_ipv6_key)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                int sourcePort = Integer.parseInt((String) newValue);

                if (sourcePort < 0 && sourcePort > 65535)
                    return false;
                String nextHopIp = sharedPreferences.getString(getString(R.string.cellular_nexthop_ipv6_key), getString(R.string.default_cellular_nexthop_ipv6));
                int nextHopPort = Integer.parseInt(sharedPreferences.getString(getString(R.string.cellular_nexthop_port_ipv6_key), getString(R.string.default_cellular_nexthop_port_ipv6)));

                NativeAccess nativeAccess = NativeAccess.getInstance();

                nativeAccess.updateInterfaceIPv6(Constants.NETDEVICE_TYPE_CELLULAR, sourcePort, nextHopIp, nextHopPort);
                return true;
            }
        });

        getPreferenceScreen().findPreference(getString(R.string.cellular_nexthop_ipv6_key)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String nextHopIp = (String) newValue;
                if (!InetAddressUtilsHC4.isIPv6Address(nextHopIp)) {
                    return false;
                }

                int sourcePort = Integer.parseInt(sharedPreferences.getString(getString(R.string.cellular_source_port_ipv6_key), getString(R.string.default_cellular_source_port_ipv6)));
                int nextHopPort = Integer.parseInt(sharedPreferences.getString(getString(R.string.cellular_nexthop_port_ipv6_key), getString(R.string.default_cellular_nexthop_port_ipv6)));

                NativeAccess nativeAccess = NativeAccess.getInstance();

                nativeAccess.updateInterfaceIPv6(Constants.NETDEVICE_TYPE_CELLULAR, sourcePort, nextHopIp, nextHopPort);
                return true;
            }
        });

        getPreferenceScreen().findPreference(getString(R.string.cellular_nexthop_port_ipv6_key)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                int nextHopPort = Integer.parseInt((String) newValue);

                if (nextHopPort < 0 && nextHopPort > 65535)
                    return false;
                int sourcePort = Integer.parseInt(sharedPreferences.getString(getString(R.string.cellular_source_port_ipv6_key), getString(R.string.default_cellular_source_port_ipv6)));

                String nextHopIp = sharedPreferences.getString(getString(R.string.cellular_nexthop_ipv6_key), getString(R.string.default_cellular_nexthop_ipv6));

                NativeAccess nativeAccess = NativeAccess.getInstance();

                nativeAccess.updateInterfaceIPv6(Constants.NETDEVICE_TYPE_CELLULAR, sourcePort, nextHopIp, nextHopPort);
                return true;
            }
        });
    }
}
