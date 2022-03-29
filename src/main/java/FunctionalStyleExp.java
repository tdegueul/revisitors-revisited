import java.util.Collection;
import java.util.stream.Collectors;

sealed interface Exp permits Lit, BinExp {}
record Lit(int n) implements Exp {}
sealed interface BinExp extends Exp permits Add, Sub {}
record Add(Exp left, Exp right) implements BinExp {}
record Sub(Exp left, Exp right) implements BinExp {}

public class FunctionalStyleExp {
	public static void main(String[] args) {
		Exp e = new Add(new Lit(2), new Sub(new Lit(5), new Lit(1)));
		System.out.println("result=" + eval(e));
	}

	static int eval(Exp e) { // Actually looks neat!
		return switch (e) {
			case Lit l -> l.n();
			case Add a -> eval(a.left()) + eval(a.right());
			case Sub s -> eval(s.left()) + eval(s.right());
		};
	}
}
