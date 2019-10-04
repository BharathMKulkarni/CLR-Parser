import java.util.regex.*;
import java.util.*;

/*
Sample input-->
S->aA
A->aA
A->b
end
*/
//Wherever there is need of char use Character instead
//Symbol everywhere is a char ,never a string
//------------------------------------------------------
public class firstFollow {
	static LinkedHashMap<Character,terminal> terminal_list = new LinkedHashMap<>(); 
	static LinkedHashMap<Character,nonTerminal> nonterminal_list = new LinkedHashMap<>();
	static List<String> productions = new ArrayList<String>();
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String input;
		while(sc.hasNextLine()) {
			input = sc.nextLine();
		    if(input.toLowerCase().equals("end") || input.toLowerCase().equals(""))break;
		    productions.add(input);
		}
		//scaned productions before this
		wrappedMain();
		for(nonTerminal x : nonterminal_list.values()) {
			printFirstFollow(x);
			}
		
	}
	public static Set<Character> compute_first(Character symbol) {
		if(terminal_list.keySet().contains(symbol))
			return new HashSet<>(Arrays.asList(symbol));
		for(String x : productions) {
			String[] prod = x.split("->",2);
			Character head = prod[0].charAt(0);
			String body = prod[1];
			
			if(head!=symbol) continue;
			if(body==""){
				nonterminal_list.get(symbol).add_first(new Character[]{(char)94}); 
				//If S-> ^ then first of S will contain ^ (char)94 , obviously!
				continue;
			}
			if(body.charAt(0)==symbol)continue;
			for(int i=0;i<body.length();i++ ) {
				Set<Character> nxt = compute_first(body.charAt(i));
				nxt.remove('^');
				nonterminal_list.get(symbol).add_first(nxt.toArray(new Character[nxt.size()]));
				//JAVA SUCKS ,above line proves it
				if(!nxt.contains((char)94)) break;
				if(i==body.length()-1)
					nonterminal_list.get(symbol).add_first(new Character[]{(char)94}); 
			}
		}
		return nonterminal_list.get(symbol).first;
	}
	public static void compute_follow(Character symbol) {
		if(symbol==nonterminal_list.keySet().toArray()[0])
			//if symbol  is the first character ie everything is derived from symbol then
			nonterminal_list.get(symbol).add_follow(new Character[] {'$'});
		for(String x : productions) {
			String[] prod = x.split("->",2);
			Character head = prod[0].charAt(0);
			String body = prod[1];	
			for(int i=0;i<body.length();i++ ) {
				Character B = body.charAt(i);
				if(B!=symbol)continue;
				if(i!=body.length()-1) {
					Set<Character> nxt = compute_first(body.charAt(i+1));
					nxt.remove('^');
					nonterminal_list.get(symbol).add_follow(nxt.toArray(new Character[nxt.size()])); 
					//JAVA SUCKS ,above line proves it
				}	
				if(i==body.length()-1 || compute_first(body.charAt(i+1)).contains('^') && B!=head) {
					Set<Character> nxt = get_follow(head);
					nonterminal_list.get(symbol).add_follow(nxt.toArray(new Character[nxt.size()]));
				    //JAVA SUCKS ,above line proves it
					}
			}
		}
	}
	public static Set<Character> get_follow(Character symbol){
		if(terminal_list.containsKey(symbol))
			return null;
		compute_follow(symbol);
		return nonterminal_list.get(symbol).follow;
	}
	public static void wrappedMain() {
		Pattern term =Pattern.compile("[a-z]");
		Pattern nonterm =Pattern.compile("[A-Z]");
		for(int i=0;i<productions.size();i++) {
			String x = productions.get(i);
			if(x.toLowerCase().equals("end") || x.toLowerCase().equals("")){
				productions.subList(i, productions.size()).clear(); 
                break;			
			}
			String[] prod = x.split("->",2);
			Character head = prod[0].charAt(0);
			String body = prod[1];	
			if(!nonterminal_list.containsKey(head)) 
				nonterminal_list.put(head, new nonTerminal(head));
			Matcher tm = term.matcher(body);
			while(tm.find()) {
				Character s = body.charAt(tm.start());	
				if(!terminal_list.containsKey(s))
					terminal_list.put(s,new terminal(s));
			}
			Matcher ntm = nonterm.matcher(body);
			while(ntm.find()) {
				Character s = body.charAt(ntm.start());	
				if(!terminal_list.containsKey(s))
					nonterminal_list.put(s,new nonTerminal(s));
			}	
			
		}
	}
	public static void printFirstFollow(nonTerminal nt) {
		compute_first(nt.symbol);
		get_follow(nt.symbol);
		System.out.print("First is of "+nt.symbol+" is :");
		nt.first.forEach(System.out::print);
		System.out.println();
		System.out.print("Follow is of "+nt.symbol+" is :");
		nt.follow.forEach(System.out::print);
		System.out.println();
	}
}
//------------------------------------------------------
class terminal{
	public char symbol;
	public terminal(char sym) {   //constructor
		this.symbol=sym;
	}
}
//------------------------------------------------------

class nonTerminal{
	public char symbol;
	Set<Character> first = new HashSet<Character>();
	Set<Character> follow = new HashSet<Character>();
	
	public nonTerminal(char sym ) {
		this.symbol= sym;
	}
	
	public void add_first(Character[] symbols) {
		List<Character> symbols_list = Arrays.asList(symbols );
		first.addAll(new HashSet<Character>(symbols_list));		
	}
	public void add_follow(Character[] symbols) {
		List<Character> symbols_list = Arrays.asList(symbols);
		follow.addAll(new HashSet<Character>(symbols_list));
	}		
	
}
  