package cn.zz.user.exception;


public interface BaseErrorCode {

    //1-9
    String LC_System = "1";
    String LC_Service = "2";
    String LC_Operation = "3";

    //01-99
    String MC_TELEUS_COMMON = "1";
    String MC_TELEUS_MAS = "2";

    String toCode();
}
