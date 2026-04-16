package com.pommala.arka.teams.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Teams routing codes. Covers team/channel routing and chat routing.
 * Prefix: {@code TEAMS-CH-*}
 */
public enum TeamsChannelCode implements ApplicationCode {

    TEAM_NOT_FOUND          ("TEAMS-CH-4000", "Team not found"),
    CHANNEL_NOT_FOUND       ("TEAMS-CH-4001", "Channel not found in team"),
    CHAT_NOT_FOUND          ("TEAMS-CH-4002", "Chat not found"),
    BOT_NOT_INSTALLED       ("TEAMS-CH-4003", "Bot is not installed in team or chat"),
    NO_VALID_TARGET         ("TEAMS-CH-4004", "No valid team/channel or chat target"),
    MEMBER_NOT_FOUND        ("TEAMS-CH-4005", "Target member not found in tenant"),
    ROUTING_AMBIGUOUS       ("TEAMS-CH-4006", "Ambiguous routing — team+channel and chat both set");

    private final String code; private final String defaultMessage;
    TeamsChannelCode(String c, String m) { code = c; defaultMessage = m; }
    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
