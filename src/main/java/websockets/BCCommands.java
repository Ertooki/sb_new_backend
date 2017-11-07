package websockets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;

public enum BCCommands {
    CONSTRUCT_SESSION{
        @Override
        public String toString() {
            JSONObject sess = new JSONObject();
            sess.put("command", "request_session");
            JSONObject params_sess = new JSONObject();
            params_sess.put("site_id", 358);
            params_sess.put("language", "eng");
            sess.put("params", params_sess);
            return sess.toString();
        }
    },

    GET_LIVE_SOCCER{
        @Override
        public String toString() {
            JSONObject get_info = new JSONObject();
            get_info.put("command","get");
            get_info.put("rid", new Integer(1));
            JSONObject params = new JSONObject();
            params.put("source", "betting");
            JSONObject what = new JSONObject();
            JSONArray empty = new JSONArray();
            what.put("sport", empty);
            what.put("region", empty);
            what.put("competition", empty);
            what.put("game", empty);
            what.put("market", empty);
            what.put("event", empty);
            params.put("what", what);
            JSONObject where = new JSONObject();
            JSONObject game = new JSONObject();
            game.put("type",new Integer(1));
            where.put("game", game);
            JSONObject sport = new JSONObject();
            JSONObject sids = new JSONObject();
            Integer[] sport_ids = {1};
            sids.put("@in", Arrays.asList(sport_ids));
            sport.put("id",sids);
            where.put("sport", sport);
            params.put("where",where);
            params.put("subscribe", false);
            get_info.put("params", params);
            return get_info.toString();
        }
    },

    GET_LIVE_HOCKEY_BASKET{
        @Override
        public String toString() {
            JSONObject get_info = new JSONObject();
            get_info.put("command","get");
            get_info.put("rid", new Integer(1));
            JSONObject params = new JSONObject();
            params.put("source", "betting");
            JSONObject what = new JSONObject();
            JSONArray empty = new JSONArray();
            what.put("sport", empty);
            what.put("region", empty);
            what.put("competition", empty);
            what.put("game", empty);
            what.put("market", empty);
            what.put("event", empty);
            params.put("what", what);
            JSONObject where = new JSONObject();
            JSONObject game = new JSONObject();
            game.put("type",new Integer(1));
            where.put("game", game);
            JSONObject sport = new JSONObject();
            JSONObject sids = new JSONObject();
            Integer[] sport_ids = {2,3};
            sids.put("@in", Arrays.asList(sport_ids));
            sport.put("id",sids);
            where.put("sport", sport);
            params.put("where",where);
            params.put("subscribe", false);
            get_info.put("params", params);
            return get_info.toString();
        }
    },

    GET_LIVE_TENNIS{
        @Override
        public String toString() {
            JSONObject get_info = new JSONObject();
            get_info.put("command","get");
            get_info.put("rid", new Integer(1));
            JSONObject params = new JSONObject();
            params.put("source", "betting");
            JSONObject what = new JSONObject();
            JSONArray empty = new JSONArray();
            what.put("sport", empty);
            what.put("region", empty);
            what.put("competition", empty);
            what.put("game", empty);
            what.put("market", empty);
            what.put("event", empty);
            params.put("what", what);
            JSONObject where = new JSONObject();
            JSONObject game = new JSONObject();
            game.put("type",new Integer(1));
            where.put("game", game);
            JSONObject sport = new JSONObject();
            JSONObject sids = new JSONObject();
            Integer[] sport_ids = {4};
            sids.put("@in", Arrays.asList(sport_ids));
            sport.put("id",sids);
            where.put("sport", sport);
            params.put("where",where);
            params.put("subscribe", false);
            get_info.put("params", params);
            return get_info.toString();
        }
    },

    GET_LIVE_OTHER{
        @Override
        public String toString() {
            JSONObject get_info = new JSONObject();
            get_info.put("command","get");
            get_info.put("rid", new Integer(1));
            JSONObject params = new JSONObject();
            params.put("source", "betting");
            JSONObject what = new JSONObject();
            JSONArray empty = new JSONArray();
            what.put("sport", empty);
            what.put("region", empty);
            what.put("competition", empty);
            what.put("game", empty);
            what.put("market", empty);
            what.put("event", empty);
            params.put("what", what);
            JSONObject where = new JSONObject();
            JSONObject game = new JSONObject();
            game.put("type",new Integer(1));
            where.put("game", game);
            JSONObject sport = new JSONObject();
            JSONObject sids = new JSONObject();
            Integer[] sport_ids = {1,2,3,4};
            sids.put("@nin", Arrays.asList(sport_ids));
            sport.put("id",sids);
            where.put("sport", sport);
            params.put("where",where);
            params.put("subscribe", false);
            get_info.put("params", params);
            return get_info.toString();
        }
    }
}
