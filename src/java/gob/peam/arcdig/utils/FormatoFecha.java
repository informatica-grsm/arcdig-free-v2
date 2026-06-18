/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author alabajos
 */
public class FormatoFecha {

    public FormatoFecha() {
    }

    public String formatear(String remain) {
        try {
            if (remain.split(" ").length > 1) {
                String aux = "";
                for (int i = 0; i < remain.split(" ").length - 1; i++) {
                    aux += remain.split(" ")[i] + " ";
                }
                remain = aux;
                return remain.replace("day", "dia").replace("mons", "meses").replace("mon", "mes").replace("years", "años").replace("year", "año");
            }

            if (Integer.parseInt(remain.split(":")[0]) <= 0) {
                if (Integer.parseInt(remain.split(":")[1]) == 0) {
                    if (Double.parseDouble(remain.split(":")[2]) > 0) {
                        return (remain.split(":")[2]) + " segundos";
                    }
                } else {
                    String min = "minuto";
                    if (Integer.parseInt(remain.split(":")[1]) > 1) {
                        min += "s";
                    }
                    return Integer.parseInt(remain.split(":")[1]) + " " + min;
                }
            } else {
                String hour = "hora";
                if (Integer.parseInt(remain.split(":")[0]) > 1) {
                    hour += "s";
                }
                return Integer.parseInt(remain.split(":")[0]) + " " + hour;
            }
            return remain;
        } catch (NumberFormatException ex) {
            return "Hace un momento";
        }
    }

    public String formatHora(String hora) {
        String fecha = "07/06/1987 " + hora;
        String formateado = "";
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fecha);
            formateado = new SimpleDateFormat("hh:mm aa").format(date);
        } catch (ParseException ex) {
        }
        return formateado;
    }

    public String formatHoraSS(String hora) {
        String fecha = "07/06/1987 " + hora;
        String formateado = "";
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(fecha);
            formateado = new SimpleDateFormat("hh:mm:ss aa").format(date);
        } catch (ParseException ex) {
        }
        return formateado;
    }

    public String formatHoraNoAA(String hora) {
        String fecha = "07/06/1987 " + hora;
        String formateado = "";
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").parse(fecha);
            formateado = new SimpleDateFormat("HH:mm").format(date);
        } catch (ParseException ex) {
        }
        return formateado;
    }

    public String formatFechaCorta(Date fecha) {
        String formateado = "";

        try {
            formateado = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
        } catch (NullPointerException ex) {
        }
        return formateado;
    }

    public Date sumarMes(Date fecha, int nroMes) {
        if (fecha == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.MONTH, nroMes);
        Date fechaa = cal.getTime();
        return fechaa;
    }

    public Date sumarMinutos(Date fecha, int nroMinutos) {
        if (fecha == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.SECOND, nroMinutos);
        Date fechaa = cal.getTime();
        return fechaa;
    }

    public String getMes(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int mes = cal.get(Calendar.MONTH);
        switch (mes) {
            case 0:
                return "Enero";
            case 1:
                return "Febrero";
            case 2:
                return "Marzo";
            case 3:
                return "Abril";
            case 4:
                return "Mayo";
            case 5:
                return "Junio";
            case 6:
                return "Julio";
            case 7:
                return "Agosto";
            case 8:
                return "Septiembre";
            case 9:
                return "Octubre";
            case 10:
                return "Noviembre";
            case 11:
                return "Diciembre";
        }
        return "no mes";
    }

    public String getMes(int fecha) {

        int mes = fecha;
        switch (mes) {
            case 0:
                return "Enero";
            case 1:
                return "Febrero";
            case 2:
                return "Marzo";
            case 3:
                return "Abril";
            case 4:
                return "Mayo";
            case 5:
                return "Junio";
            case 6:
                return "Julio";
            case 7:
                return "Agosto";
            case 8:
                return "Septiembre";
            case 9:
                return "Octubre";
            case 10:
                return "Noviembre";
            case 11:
                return "Diciembre";
        }
        return "no mes";
    }

    public boolean esMismoDia(Date fecha1, Date fecha2) {
        if (fecha1 == null || fecha2 == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser null");
        }
        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(fecha1);
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(fecha2);
        return esMismoDia(cal1, cal2);
    }

    public boolean esMismoDia(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public String getFechaDetail(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        if (dia == 1) {
            return "al primer día del mes de " + getMes(fecha) + " del Año " + cal.get(Calendar.YEAR);
        } else {
            return "a los " + dia + " días del mes de " + getMes(fecha) + " del Año " + cal.get(Calendar.YEAR);
        }
    }

    public Date getUltimoDia(Date fecha) {

        Date fechaa = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int anho = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        String[] mesesAnio = new String[13];

        mesesAnio[0] = "31";
        if ((anho % 4 == 0) && ((anho % 100 != 0) || (anho % 400 == 0))) {
            mesesAnio[1] = "29";
        } else {
            mesesAnio[1] = "28";
        }

        mesesAnio[2] = "31";
        mesesAnio[3] = "30";
        mesesAnio[4] = "31";
        mesesAnio[5] = "30";
        mesesAnio[6] = "31";
        mesesAnio[7] = "31";
        mesesAnio[8] = "30";
        mesesAnio[9] = "31";
        mesesAnio[10] = "30";
        mesesAnio[11] = "31";

        try {
            fechaa = new SimpleDateFormat("dd/MM/yyyy").parse(mesesAnio[mes] + "/" + (mes + 1) + "/" + anho);
        } catch (ParseException ex) {
        }

        return fechaa;
    }

    public Date formatStringFechaCorta(String fecha) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
        } catch (ParseException ex) {
        }
        return date;
    }

    public long diferenciaDias(Date Dinicio, Date Dfinal) {
        final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día 

        long diferencia = (Dfinal.getTime() - Dinicio.getTime());

        return diferencia;
    }

    public long diferenciaMinutos(Date fechaInicial, Date fechaFinal) {
        long totalMinutos = 0;
        Calendar a = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        a.setTime(fechaInicial);
        b.setTime(fechaFinal);

        totalMinutos = ((b.getTimeInMillis() - a.getTimeInMillis()) / 1000 / 60);

        return totalMinutos;

    }

    public long getDiferenciaDias(Date Dinicio, Date Dfinal) {
        Calendar calInicio = Calendar.getInstance();
        Calendar calFinal = Calendar.getInstance();
        Calendar auxCalInicio = Calendar.getInstance();

        calInicio.setTime(Dinicio);
        auxCalInicio.setTime(Dinicio);
        calFinal.setTime(Dfinal);

        Integer diferencia = 0;

        while (calInicio.before(calFinal)) {
            diferencia++;
            calInicio.setTime(Dinicio);
            calInicio.add(Calendar.DAY_OF_YEAR, diferencia);
        }

        return diferencia;
    }

    public String formatFechaHora(Date fecha) {
        String formateado = "";
        formateado = new SimpleDateFormat("hh:mm aa").format(fecha);
        return formateado;
    }

    public String formatFechaHoraNoAA(Date fecha) {
        String formateado = "";
        formateado = new SimpleDateFormat("HH:mm").format(fecha);
        return formateado;
    }

    public String formatHoraNoSSAA(String hora) {
        String fecha = "07/06/1987 " + hora;
        String formateado = "";
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa").parse(fecha);
            formateado = new SimpleDateFormat("HH:mm").format(date);
        } catch (ParseException ex) {
        }
        return formateado;
    }

    public Integer calcularEdad(Date fechaNac) {

        Calendar fechaNacimiento = Calendar.getInstance();
        //Se crea un objeto con la fecha actual
        Calendar fechaActual = Calendar.getInstance();
        //Se asigna la fecha recibida a la fecha de nacimiento.
        fechaNacimiento.setTime(fechaNac);
        //Se restan la fecha actual y la fecha de nacimiento
        int anho = fechaActual.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);
        int mes = fechaActual.get(Calendar.MONTH) - fechaNacimiento.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DATE) - fechaNacimiento.get(Calendar.DATE);
        //Se ajusta el año dependiendo el mes y el día
        if (mes < 0 || (mes == 0 && dia < 0)) {
            anho--;
        }
        //Regresa la edad en base a la fecha de nacimiento
        return anho;
    }

    public Date getHora(Date fechaHora, String hora) {
        String formateado = new SimpleDateFormat("dd/MM/yyyy").format(fechaHora) + " " + hora;

        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(formateado);

        } catch (ParseException ex) {
        }
        return date;
    }

    public String formatHoraMM(Date date) {
        String formateado = null;
        formateado = new SimpleDateFormat("hh:mm:ss aa ").format(date);
        return formateado;
    }

    public long diferenciaMinutosX(Date fechaInicial, Date fechaFinal) {
        long totalMinutos = 0;
        try {
            Calendar a = Calendar.getInstance();
            Calendar b = Calendar.getInstance();
            a.setTime(fechaInicial);
            b.setTime(fechaFinal);

            totalMinutos = ((b.getTimeInMillis() - a.getTimeInMillis()) / 1000 / 60);
            if (totalMinutos < 0) {
                return (totalMinutos * -1);
            } else {
                return (totalMinutos);
            }
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public String fileModificated(long ms) {
        Date d = new Date(ms);
        Calendar c = Calendar.getInstance();
        int mes_=1;
        if(System.getProperty("os.name").contains("Windows")){
            mes_ = 1;
        }
        c.setTime(d);
        c.add(Calendar.MONTH, mes_);
        String dia = Integer.toString(c.get(Calendar.DATE));
        String mes = Integer.toString(c.get(Calendar.MONTH));
        String annio = Integer.toString(c.get(Calendar.YEAR));
        String hora = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        String minuto = Integer.toString(c.get(Calendar.MINUTE));
        String segundo = Integer.toString(c.get(Calendar.SECOND));
        
        return dia + "/" + mes + "/" + annio + " " + hora + ":" + minuto;
    }

    public Date restarDia(int dias, Date Dfinal) {
        Calendar calInicio = Calendar.getInstance();

        calInicio.setTime(Dfinal);

        calInicio.add(Calendar.DAY_OF_YEAR, -dias);

        Date nuevo = calInicio.getTime();

        return nuevo;
    }

    public String fechayHora(Date date) {
        String formateado = null;
        formateado = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa ").format(date);
        return formateado;
    }
}
