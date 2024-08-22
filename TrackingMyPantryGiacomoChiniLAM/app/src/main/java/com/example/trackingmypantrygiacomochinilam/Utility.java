package com.example.trackingmypantrygiacomochinilam;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class Utility {

    //converte calendar in una stringa formato gg/mm//yy
    public static String getDateStringFromCalendar(Calendar cal){
        //ritorna string in formato day/month/year
        int month = cal.get(Calendar.MONTH);
        month++;
        return cal.get(Calendar.DAY_OF_MONTH)+"/"+month+"/"+cal.get(Calendar.YEAR);
    }

    //ritorna true se c'è bisogno di autenticarsi di nuovo oppure no (in base alla durata token)
    public static boolean needAuthentication(int last){
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if(today >= last){
            if(today-last > 5){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromBytes(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static String getTokenPref(Context context){
        SharedPreferences sP1 = context.getSharedPreferences("REMEMBER_PREF", Context.MODE_PRIVATE);
        String idPref = sP1.getString("CURRENT_ID_PREF","");
        if(!idPref.equals("")){
            SharedPreferences sP2 = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            String token = sP2.getString("ACCESS_TOKEN_PREF", "");
            if(!token.equals("")){
                return token;
            }
        }
        return "";
    }

    public static String getIdPref(Context context){
        SharedPreferences sP1 = context.getSharedPreferences("REMEMBER_PREF", Context.MODE_PRIVATE);
        String idPref = sP1.getString("CURRENT_ID_PREF","");
        return idPref;
    }

    public static Integer getQuantityPref(Context context){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            int value = sp.getInt("QUANTITY_PREF", 1);
            return value;
        }
        return 1;
    }
    public static void setQuantityPref(Context context, int value){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("QUANTITY_PREF", value);
            editor.commit();
        }
    }

    public static Integer getQualityPref(Context context){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            int value = sp.getInt("QUALITY_PREF", 1);
            return value;
        }
        return 1;
    }
    public static void setQualityPref(Context context, int value){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("QUALITY_PREF", value);
            editor.commit();
        }
    }

    public static Integer getGGPref(Context context){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            int value = sp.getInt("GG_PREF", 1);
            return value;
        }
        return 1;
    }
    public static void setGGPref(Context context, int value){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("GG_PREF", value);
            editor.commit();
        }
    }

    public static Integer getMMPref(Context context){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            int value = sp.getInt("MM_PREF", 1);
            return value;
        }
        return 1;
    }
    public static void setMMPref(Context context, int value){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("MM_PREF", value);
            editor.commit();
        }
    }

    public static Integer getYYPref(Context context){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            int value = sp.getInt("YY_PREF", 1);
            return value;
        }
        return 1;
    }
    public static void setYYPref(Context context, int value){
        String idPref = getIdPref(context);
        if(!idPref.equals("")){
            SharedPreferences sp = context.getSharedPreferences(idPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("YY_PREF", value);
            editor.commit();
        }
    }

    public static void disableAutomatedAccess(Context context){
        SharedPreferences sp = context.getSharedPreferences("REMEMBER_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("REMEMBER_ID_PREF","");
        editor.commit();
    }

}
