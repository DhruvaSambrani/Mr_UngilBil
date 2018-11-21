package com.dhruva.ungeelbill;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

class SketchwareUtil {
    static void showMessage(Context _context, String _s) {
        Toast.makeText(_context, _s, Toast.LENGTH_SHORT).show();
    }

    static void getAllKeysFromMap(Map<String, Object> map, ArrayList<String> output) {
        if (output == null) return;
        output.clear();
        if (map == null || map.size() <= 0) return;
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            output.add((String) entry.getKey());
        }
    }
}