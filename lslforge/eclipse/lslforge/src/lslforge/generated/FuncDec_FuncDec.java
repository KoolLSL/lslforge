package lslforge.generated;
import com.thoughtworks.xstream.XStream;
import java.util.LinkedList;
@SuppressWarnings("unused")
public class FuncDec_FuncDec extends FuncDec{
    public Ctx<String> funcName;
    public LSLType funcType;
    public LinkedList<Ctx<Var>> funcParms;
    public static void init(XStream xstream) {
        xstream.alias("FuncDec_FuncDec",FuncDec_FuncDec.class); //$NON-NLS-1$
    }
}
