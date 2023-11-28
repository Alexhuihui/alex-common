package top.alexmmd.common.base.web.support;

public interface IDGenerator {

    public static final String ID_FIELD = "id";

    public Long longId();

    public String longStringId();

    public String stringId();

}
