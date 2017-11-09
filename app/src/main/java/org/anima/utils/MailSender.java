package org.anima.utils;

import android.content.Context;
import android.content.res.Resources;
//import com.kams.android.R;
//import com.kams.android.database.DBAdapter;
//import com.kams.android.models.EvaluationResult;
//import com.kams.android.models.Users;

import java.util.Properties;

/**
 * Handle the mail sending when the time is up
 * Created by BeugFallou on 12/12/2014.
 */
public final class MailSender {

    public static final String MAIL_SUBJECT = "Android Technical Test of ";
    public static final String MAIL_BODY_TITLE = "The user (below) just finishes the Android Technical Test.\r\n\r\n";

    public static String getProperty(Context context, String PropertyName)
    {
        // Read from the /res/raw directory
        try {
            String myProperty;
            Resources resources = context.getResources();

           // InputStream rawResource = resources.openRawResource(R.drawable.config);
            Properties properties = new Properties();
            //properties.load(rawResource);

            myProperty = properties.getProperty(PropertyName);

            return myProperty;
        } catch (Resources.NotFoundException e) {
            System.err.println("Did not find raw resource : "+e);

            return "";
        }
    }
    /*

    public static String getMailObject(Users users){
        return MAIL_SUBJECT + users.getUsrName()+" "+users.getUsrLastName();
    }


    public static String getMailBody(Users users, int evaluationResult, String date){
        String mailBody = MAIL_BODY_TITLE;
        mailBody += "   Name : "+users.getUsrName()+"\r\n";
        mailBody += "   Last Name : "+users.getUsrLastName()+"\r\n";
        if(!users.getUsrAddress().isEmpty()){
            mailBody += "   Address : "+users.getUsrAddress()+"\r\n";
        }
        mailBody += "   Phone : "+users.getUsrPhone()+"\r\n";
        mailBody += "   Date & Time : "+date+"\r\n\r\n";
        mailBody += "      > The result is : "+evaluationResult+"/30\r\n\r\n";
        mailBody += "Regards.\r\n\r\n";
        mailBody += "Android Application";
        return mailBody;
    }
    */
    /*

    public static boolean send(Context context, int evaluationResult){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateandTime = sdf.format(new Date());
        String body = getMailBody(users, evaluationResult, currentDateandTime);
        String subject = getMailObject(users);

        HashMap<String, String> map = EvaluationPreferences.loadManagerAccess(context);
        if(map != null){
            String usr = map.get(EvaluationPreferences.MGR_MAIL);
            String passw = map.get(EvaluationPreferences.MGR_PASSWORD);

            GMailSender sender = new GMailSender(usr, passw);
            try {
                sender.sendMail(subject,
                        body,
                        usr,
                        getProperty(context, "MAIL_TO"));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
        return false;

    }

    */

    /*

    public static boolean send(Context context, Users users, int evaluationResult, String date){
        String body = getMailBody(users, evaluationResult, date);
        String subject = getMailObject(users);

        HashMap<String, String> map = EvaluationPreferences.loadManagerAccess(context);
        if(map != null){
            String usr = map.get(EvaluationPreferences.MGR_MAIL);
            String passw = map.get(EvaluationPreferences.MGR_PASSWORD);

            GMailSender sender = new GMailSender(usr, passw);
            try {
                sender.sendMail(subject,
                        body,
                        usr,
                        getProperty(context, "MAIL_TO"));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
        return false;

    }
    */
    /*

    public static boolean sendLastEvaluationResult(Context context, DBAdapter db){
        ArrayList<EvaluationResult> lsEvaluationToSend = db.selectEvaluationResult();
        if(lsEvaluationToSend != null){
            for(int cpt = 0; cpt < lsEvaluationToSend.size(); cpt++){
                send(context, lsEvaluationToSend.get(cpt).getUser(),
                        lsEvaluationToSend.get(cpt).getResult(),
                        lsEvaluationToSend.get(cpt).getEvaluationDate());
            }

        }
        return false;
    }

    */
    /*

    public static boolean sendTestMailWithGmailSender(Context context, String usr, String passw){
        GMailSender sender = new GMailSender();
        String body = "address and password valid";
        String subject = "User authentication";
        try {
            sender.sendMail(subject,
                    body,
                    usr,
                    getProperty(context, "MAIL_TEST_TO"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    */
}
