/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.rcp.ui.utils;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javafx.scene.input.DataFormat;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
//import org.codehaus.jackson.map.util.StdDateFormat;

/**
 * Esta classe apresenta metodos que facilitam o uso de datas na aplicação
 *
 * @author Marcelo Lopes da Silva - 647497 UTFPR
 */
public class DateUtils {

    private static final String ESPACADOR = "/";
    private static Collection<DateListener> dateListeners;

    static {
        dateListeners = new ArrayList<>();
    }

    public static void DateListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DateUtils() {
        dateListeners = new ArrayList<>();
    }

//    ResourceBundle rb = ResourceBundle.getBundle("sistemaestagio/utils/Bundle");
    /**
     * Retorna a data atual do sistema
     *
     * @return
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * Recebe por parametro a data no formato String e retorna uma data no
     * formato Date
     *
     * @param args
     * @throws java.text.ParseException
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws ParseException, Exception {
        System.out.println(getDate("120112"));
    }

    public static Date getDate2(String text) {
        try {
            return getDate(text);
        } catch (RuntimeException | ParseException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static String getNow(String format) {
        try {
            Date now = new Date();
            DateFormat df = new SimpleDateFormat(format);
            return df.format(now);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

	public static Date getDate(String data) throws ParseException, RuntimeException {
        if (!validaData(data)) {
            Calendar calendar = GregorianCalendar.getInstance();
            int mes = calendar.get(Calendar.MONTH) + 1;
            int ano = calendar.get(Calendar.YEAR);

            StringBuilder sb = new StringBuilder();

            //      só um falor entre 1 e 31 referente ao dia do mês      
            String redex1 = "([1-9]|0[1-9]|1[0-9]|2[0-9]|3[01])";
            String redex2 = "([1-9]|[0-2][0-9]|3[01])([/.-|])([1-9]|[0][1-9]|1[012])";
            String redex3 = "([0-2][0-9]|3[01])([/.-]|)([0][0-9]|1[012])\\2(\\d{1,4})";
            String redex4 = "([0-3]|[012][0-9]|3[01])([1-9]|0[0-9]|1[012])(\\d{1,4})([01][0-9]|2[0-3])"; //12.12.2012 23
            String redex5 = "([0-3]|[012][0-9]|3[01])([1-9]|0[0-9]|1[012])(\\d{1,4})([01][0-9]|2[0-3])([0-5][0-9])"; //12.12.2012 23:32
            String redex6 = "([0-3]|[012][0-9]|3[01])([1-9]|0[0-9]|1[012])(\\d{1,4})([01][0-9]|2[0-3])([0-5][0-9])([0-5][0-9])"; //12.12.2012 23:32:32
//            String redex7 = "(\\d{1,4})([01][0-9]|2[0-3])([0-5][0-9])([0-5][0-9])"; //23:32:32
//            String redex8 = "(\\d{1,4})([01][0-9]|2[0-3])([0-5][0-9])"; //23:32

            data = data.replace("/", "");
            data = data.replace("-", "");
            data = data.replace(".", "");
            data = data.replace(" ", "");
            data = data.replace(":", "");
            data = data.replace("'", "");

//             if (data.matches(redex7)||data.matches(redex8)) {
//                //23:32:32
//                if (data.length() == 3) {
//                    return new SimpleDateFormat("Hmm").parse(data);
//                }
//                if (data.length() == 4) {
//                    return new SimpleDateFormat("HHmm").parse(data);
//                }
//                if (data.length() == 5) {
//                    return new SimpleDateFormat("Hmmss").parse(data);
//                }
//                if (data.length() == 6) {
//                    return new SimpleDateFormat("HHmmss").parse(data);
//                }
//            }
            if (data.matches(redex1)) {
                sb.append(String.format("%02d", Integer.parseInt(data)));
                sb.append(ESPACADOR);

                sb.append(String.format("%02d", mes));

                sb.append(ESPACADOR);

                sb.append(ano);
            }

            if (data.matches(redex2)) {

                if (data.length() == 3) {
                    sb.append(String.format("%02d", Integer.parseInt(data.substring(0, 2))));

                    sb.append(ESPACADOR);

                    sb.append(String.format("%02d", Integer.parseInt(data.substring(2, 3))));

                    sb.append(ESPACADOR);

                    sb.append(ano);
                }

                if (data.length() == 4) {
                    sb.append(String.format("%02d", Integer.parseInt(data.substring(0, 2))));

                    sb.append(ESPACADOR);

                    sb.append(String.format("%02d", Integer.parseInt(data.substring(2, 4))));

                    sb.append(ESPACADOR);

                    sb.append(ano);
                }
            }
            if (data.matches(redex3)) {

                sb.append(String.format("%02d", Integer.parseInt(data.substring(0, 2))));

                sb.append(ESPACADOR);

                sb.append(String.format("%02d", Integer.parseInt(data.substring(2, 4))));

                sb.append(ESPACADOR);

                ano = Integer.parseInt(data.substring(4, data.length()));
                if (data.substring(4, data.length()).length() > 2) {
                    sb.append(ano);
                } else {
                    if (ano < 25) {
                        ano += 2000;
                    } else {
                        ano += 1900;
                    }
                    sb.append(ano);
                }

            }

            if (data.matches(redex4)) {
                if (data.length() == 10) {
                    return new SimpleDateFormat("ddMMyyyyHH").parse(data);
                }
                if (data.length() == 8) {
                    Date dateret = new SimpleDateFormat("ddMMyyHH").parse(data);
                    if (dateret.after(getDate("1900", "yyyy"))) {
                        calendar.setTime(dateret);
                        calendar.add(Calendar.YEAR, 1900);
                    }
                    return dateret;
                }
            }

            if (data.matches(redex5)) {

                if (data.length() == 10) {
                    return new SimpleDateFormat("ddMMyyHHmm").parse(data);
                }
                if (data.length() == 12) {
                    return new SimpleDateFormat("ddMMyyyyHHmm").parse(data);
                }
            }
            if (data.matches(redex6)) {
                if (data.length() == 12) {
                    return new SimpleDateFormat("ddMMyyHHmmss").parse(data);
                }
                if (data.length() == 14) {
                    return new SimpleDateFormat("ddMMyyyyHHmmss").parse(data);
                }
            }

            if (validaData(sb.toString())) {
                return new SimpleDateFormat("dd/MM/yyyy").parse(sb.toString());
            }

            throw new RuntimeException("Data inválida!");
        }
        return new SimpleDateFormat("dd/MM/yyyy").parse(data);
    }

    /**
     * Recebe por parametro a data no formato String e retorna uma data no
     * formato Date
     *
     * @param formato é a formato em que os dados estão sendo informados
     * Exemplo:<br /> "dd/MM/yyyy"
     * @param data é a data no formato string que será convertida para Date()
     * @return um valor Date()
     * @throws java.text.ParseException
     */
    public static Date getDate(String data, String formato) throws ParseException {
        return new SimpleDateFormat(formato).parse(data);
    }
    
    
    

    public static Date getDateFormats(String date, String... formats) {
        Date ret;
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                ret = sdf.parse(date);
                return ret;
            } catch (Exception e) {
            }
        }
        throw new UnsupportedOperationException("error.invalid.date.to.format");
    }

    /**
     *
     * @param formato
     * @param data
     * @return
     * @throws Exception
     */
    public static String getDateString(String data, String formato) throws Exception {
        formato = formato == null ? "dd/MM/yyyy" : formato;
        data = data == null ? new Date().toString() : data;

        return new SimpleDateFormat(formato).parse(data).toString();
    }

    public static Date getDateNow() throws RuntimeException {
        TimeZone tz = TimeZone.getTimeZone("GMT-3");
        return getDateNow(tz);
    }

    public static Date getDateNow(TimeZone tz) throws RuntimeException {
        Calendar now = Calendar.getInstance(tz);
        try {
//            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//            format.setTimeZone(tz);
            Calendar ca = GregorianCalendar.getInstance();
            ca.set(Calendar.DATE, now.get(Calendar.DATE));
            ca.setTimeZone(tz);
            return ca.getTime();

        } catch (Exception ex) {
            Logger.getLogger(DateUtils.class.getName()).log(Level.SEVERE, null, ex);
            return now.getTime();
        }

    }

    public static String getDateString(Date data) throws Exception {
        return new SimpleDateFormat("dd/MM/yyyy").format(data);
//        return new SimpleDateFormat("dd/MM/yyyy").parse(data).toString();
    }
    final static long MSEC_PER_HOUR = 1000L * 60L * 60L; // Numero de milisegundos numa hora
    public final static long MSEC_PER_DAY = MSEC_PER_HOUR * 24L; // Numero de milisegundos ao dia 

    public static Long getIdade(Date dataNascimento) throws Exception {

        long difDay = ((new Date().getTime() - dataNascimento.getTime()) / (24 * 60 * 60 * 1000));
        difDay = (long) (difDay / 365.25);

        return difDay;
    }

    public static String getDiaDaSemana(Date data) throws Exception, ParseException {
        Date d = data;
        String[] dias = new String[]{"Sabado", "Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira"};

        Calendar calendario = new GregorianCalendar();
        calendario.setTime(d);

        return dias[calendario.get(Calendar.DAY_OF_WEEK)];

    }

    public static boolean validaData(String data)  {
        Boolean ret;
        String DatePattern = "^"
                + "(?:(31)(\\D)(0?[13578]|1[02])\\2|(29|30)(\\D)(0?[13-9]|1[0-2])\\5|(0?[1-9]|1\\d|2[0-8])(\\D)(0?[1-9]|1[0-2])\\8)"
                + "((?:1[6-9]|[2-9]\\d)?\\d{2})$|"
                + "^"
                + "(29)(\\D)(0?2)\\12((?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)$";

        ret = data.matches(DatePattern);
        return ret;
    }

    public static boolean validaData(Date data) throws Exception {
        return validaData(dateToString(data));

    }

    public static long getIntervaloDias(Date in, Date end) {

        long diff = ((in.getTime() - end.getTime()) / (24 * 60 * 60 * 1000));
//        diff = (long) (diff / 365.25);

        return diff;

    }

    public Calendar getCalendar(Date date) {
        GregorianCalendar ret = new GregorianCalendar();
        ret.setTime(date);
        return ret;
    }

    public static String dateToString(Date data, String format) throws RuntimeException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String dateString = dateFormat.format(data);
        return dateString;
    }

    public static String dateToString(Date data) throws Exception {
        return dateToString(data, "dd/mm/yyyy");
    }

    public static void addJTextComponentDate(JTextComponent component) {
        component.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                try {
                    JTextComponent comp = (JTextComponent) input;
                    if (comp.getText().isEmpty()) {
                        return true;
                    } else {
                        Date date = getDate(comp.getText());
                        comp.setText(getDateString(date));
                        return true;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(input, e.getMessage(), "Erro no campo de data", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        });
    }

    public static void removeDateListener(DateListener dl) {
        dateListeners.remove(dl);
    }

    public static void addDateListener(DateListener dl) {
        if (!dateListeners.contains(dl)) {
            dateListeners.add(dl);
        }
    }

    public interface DateListener {

        void processResult(Date date, JTextComponent source);

        void dateError(Throwable e, Object source);
    }
}
