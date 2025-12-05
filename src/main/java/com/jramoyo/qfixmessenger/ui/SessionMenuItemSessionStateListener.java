package com.jramoyo.qfixmessenger.ui;

import quickfix.SessionStateListener;

import javax.swing.*;

public class SessionMenuItemSessionStateListener implements SessionStateListener {

  private final JMenuItem seqNumMenuItem;

  private final JMenuItem resetMenuItem;

  public SessionMenuItemSessionStateListener(JMenuItem seqNumMenuItem, JMenuItem resetMenuItem) {
        this.seqNumMenuItem = seqNumMenuItem;
        this.resetMenuItem = resetMenuItem;
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onLogon() {
      resetMenuItem.setEnabled(false);
      seqNumMenuItem.setEnabled(false);
    }

    @Override
    public void onLogout() {
        resetMenuItem.setEnabled(true);
        seqNumMenuItem.setEnabled(true);
    }

    @Override
    public void onReset() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onMissedHeartBeat() {

    }

    @Override
    public void onHeartBeatTimeout() {

    }
}
