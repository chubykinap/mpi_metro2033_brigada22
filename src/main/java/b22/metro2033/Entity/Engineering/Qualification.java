package b22.metro2033.Entity.Engineering;

import javassist.NotFoundException;

public enum Qualification{
    THIRD,
    SECOND,
    FIRST;

    private final static String[] qualificationRU = new String[]{
            "Третья степень", "Вторая степень", "Первая степень"
    };

    public static String[] getQualificationRU(){ return qualificationRU; }

    public static String getStateRU(Qualification state){
        return qualificationRU[state.ordinal()];
    }

    public static Qualification findState(String state) throws Exception{
        switch (state){
            case "Третья степень":
                return THIRD;
            case "Вторая степень":
                return  SECOND;
            case "Первая степень":
                return FIRST;
        }
        throw new NotFoundException("STATE NOT FOUND");
    }
}