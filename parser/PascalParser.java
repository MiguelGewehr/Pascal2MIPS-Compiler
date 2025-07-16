// Generated from PascalParser.g by ANTLR 4.13.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class PascalParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PLUS=1, MINUS=2, STAR=3, SLASH=4, EQUAL=5, LESS=6, GREATER=7, LBRACK=8, 
		RBRACK=9, DOT=10, COMMA=11, COLON=12, SEMI=13, QUOTE=14, LPAREN=15, RPAREN=16, 
		NOTEQUAL=17, LESSEQUAL=18, GREATEREQUAL=19, ASSIGN=20, DOTDOT=21, LBRACK_ALT=22, 
		RBRACK_ALT=23, AT=24, AND=25, ARRAY=26, BEGIN=27, CASE=28, CONST=29, DIV=30, 
		DO=31, DOWNTO=32, ELSE=33, END=34, FILE=35, FOR=36, FUNCTION=37, GOTO=38, 
		IF=39, IN=40, LABEL=41, MOD=42, NIL=43, NOT=44, OF=45, OR=46, PACKED=47, 
		PROCEDURE=48, PROGRAM=49, RECORD=50, REPEAT=51, SET=52, THEN=53, TO=54, 
		TYPE=55, UNTIL=56, VAR=57, WHILE=58, WITH=59, BOOLEAN=60, TRUE=61, FALSE=62, 
		WS=63, INTEGER=64, REAL=65, CHARACTER=66, STRING=67, IDENTIFIER=68, COMMENT1=69, 
		COMMENT2=70;
	public static final int
		RULE_program = 0, RULE_identifierList = 1, RULE_block = 2, RULE_labelSection = 3, 
		RULE_label = 4, RULE_constSection = 5, RULE_constDefinition = 6, RULE_constant = 7, 
		RULE_signedNumber = 8, RULE_typeSection = 9, RULE_typeDefinition = 10, 
		RULE_typeDenoter = 11, RULE_indexRange = 12, RULE_varSection = 13, RULE_varDeclaration = 14, 
		RULE_subroutineDeclarationPart = 15, RULE_procedureDeclaration = 16, RULE_functionDeclaration = 17, 
		RULE_formalParameterList = 18, RULE_formalParameterSection = 19, RULE_compoundStatement = 20, 
		RULE_statementList = 21, RULE_statement = 22, RULE_assignmentStatement = 23, 
		RULE_procedureCall = 24, RULE_ifStatement = 25, RULE_whileStatement = 26, 
		RULE_emptyStatement = 27, RULE_expression = 28, RULE_simpleExpression = 29, 
		RULE_term = 30, RULE_factor = 31, RULE_variable = 32, RULE_expressionList = 33, 
		RULE_expressionItem = 34, RULE_formattedExpression = 35, RULE_repeatStatement = 36, 
		RULE_forStatement = 37;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "identifierList", "block", "labelSection", "label", "constSection", 
			"constDefinition", "constant", "signedNumber", "typeSection", "typeDefinition", 
			"typeDenoter", "indexRange", "varSection", "varDeclaration", "subroutineDeclarationPart", 
			"procedureDeclaration", "functionDeclaration", "formalParameterList", 
			"formalParameterSection", "compoundStatement", "statementList", "statement", 
			"assignmentStatement", "procedureCall", "ifStatement", "whileStatement", 
			"emptyStatement", "expression", "simpleExpression", "term", "factor", 
			"variable", "expressionList", "expressionItem", "formattedExpression", 
			"repeatStatement", "forStatement"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'+'", "'-'", "'*'", "'/'", "'='", "'<'", "'>'", "'['", "']'", 
			"'.'", "','", "':'", "';'", "'''", "'('", "')'", "'<>'", "'<='", "'>='", 
			"':='", "'..'", "'(.'", "'.)'", "'@'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PLUS", "MINUS", "STAR", "SLASH", "EQUAL", "LESS", "GREATER", "LBRACK", 
			"RBRACK", "DOT", "COMMA", "COLON", "SEMI", "QUOTE", "LPAREN", "RPAREN", 
			"NOTEQUAL", "LESSEQUAL", "GREATEREQUAL", "ASSIGN", "DOTDOT", "LBRACK_ALT", 
			"RBRACK_ALT", "AT", "AND", "ARRAY", "BEGIN", "CASE", "CONST", "DIV", 
			"DO", "DOWNTO", "ELSE", "END", "FILE", "FOR", "FUNCTION", "GOTO", "IF", 
			"IN", "LABEL", "MOD", "NIL", "NOT", "OF", "OR", "PACKED", "PROCEDURE", 
			"PROGRAM", "RECORD", "REPEAT", "SET", "THEN", "TO", "TYPE", "UNTIL", 
			"VAR", "WHILE", "WITH", "BOOLEAN", "TRUE", "FALSE", "WS", "INTEGER", 
			"REAL", "CHARACTER", "STRING", "IDENTIFIER", "COMMENT1", "COMMENT2"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "PascalParser.g"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PascalParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode PROGRAM() { return getToken(PascalParser.PROGRAM, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode SEMI() { return getToken(PascalParser.SEMI, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode DOT() { return getToken(PascalParser.DOT, 0); }
		public TerminalNode LPAREN() { return getToken(PascalParser.LPAREN, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(PascalParser.RPAREN, 0); }
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			match(PROGRAM);
			setState(77);
			match(IDENTIFIER);
			setState(82);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(78);
				match(LPAREN);
				setState(79);
				identifierList();
				setState(80);
				match(RPAREN);
				}
			}

			setState(84);
			match(SEMI);
			setState(85);
			block();
			setState(86);
			match(DOT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdentifierListContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(PascalParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(PascalParser.IDENTIFIER, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PascalParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PascalParser.COMMA, i);
		}
		public IdentifierListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitIdentifierList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierListContext identifierList() throws RecognitionException {
		IdentifierListContext _localctx = new IdentifierListContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_identifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(IDENTIFIER);
			setState(93);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(89);
				match(COMMA);
				setState(90);
				match(IDENTIFIER);
				}
				}
				setState(95);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public LabelSectionContext labelSection() {
			return getRuleContext(LabelSectionContext.class,0);
		}
		public ConstSectionContext constSection() {
			return getRuleContext(ConstSectionContext.class,0);
		}
		public TypeSectionContext typeSection() {
			return getRuleContext(TypeSectionContext.class,0);
		}
		public VarSectionContext varSection() {
			return getRuleContext(VarSectionContext.class,0);
		}
		public List<SubroutineDeclarationPartContext> subroutineDeclarationPart() {
			return getRuleContexts(SubroutineDeclarationPartContext.class);
		}
		public SubroutineDeclarationPartContext subroutineDeclarationPart(int i) {
			return getRuleContext(SubroutineDeclarationPartContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LABEL) {
				{
				setState(96);
				labelSection();
				}
			}

			setState(100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CONST) {
				{
				setState(99);
				constSection();
				}
			}

			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TYPE) {
				{
				setState(102);
				typeSection();
				}
			}

			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(105);
				varSection();
				}
			}

			setState(111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FUNCTION || _la==PROCEDURE) {
				{
				{
				setState(108);
				subroutineDeclarationPart();
				}
				}
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(114);
			compoundStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabelSectionContext extends ParserRuleContext {
		public TerminalNode LABEL() { return getToken(PascalParser.LABEL, 0); }
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public TerminalNode SEMI() { return getToken(PascalParser.SEMI, 0); }
		public List<TerminalNode> COMMA() { return getTokens(PascalParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PascalParser.COMMA, i);
		}
		public LabelSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labelSection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitLabelSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelSectionContext labelSection() throws RecognitionException {
		LabelSectionContext _localctx = new LabelSectionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_labelSection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(LABEL);
			setState(117);
			label();
			setState(122);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(118);
				match(COMMA);
				setState(119);
				label();
				}
				}
				setState(124);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(125);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabelContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(PascalParser.INTEGER, 0); }
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstSectionContext extends ParserRuleContext {
		public TerminalNode CONST() { return getToken(PascalParser.CONST, 0); }
		public List<ConstDefinitionContext> constDefinition() {
			return getRuleContexts(ConstDefinitionContext.class);
		}
		public ConstDefinitionContext constDefinition(int i) {
			return getRuleContext(ConstDefinitionContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(PascalParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(PascalParser.SEMI, i);
		}
		public ConstSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constSection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitConstSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstSectionContext constSection() throws RecognitionException {
		ConstSectionContext _localctx = new ConstSectionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_constSection);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			match(CONST);
			setState(130);
			constDefinition();
			setState(135);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(131);
					match(SEMI);
					setState(132);
					constDefinition();
					}
					} 
				}
				setState(137);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(138);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode EQUAL() { return getToken(PascalParser.EQUAL, 0); }
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public ConstDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitConstDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstDefinitionContext constDefinition() throws RecognitionException {
		ConstDefinitionContext _localctx = new ConstDefinitionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_constDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			match(IDENTIFIER);
			setState(141);
			match(EQUAL);
			setState(142);
			constant();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstantContext extends ParserRuleContext {
		public SignedNumberContext signedNumber() {
			return getRuleContext(SignedNumberContext.class,0);
		}
		public TerminalNode CHARACTER() { return getToken(PascalParser.CHARACTER, 0); }
		public TerminalNode STRING() { return getToken(PascalParser.STRING, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode TRUE() { return getToken(PascalParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(PascalParser.FALSE, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_constant);
		try {
			setState(150);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PLUS:
			case MINUS:
			case INTEGER:
			case REAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(144);
				signedNumber();
				}
				break;
			case CHARACTER:
				enterOuterAlt(_localctx, 2);
				{
				setState(145);
				match(CHARACTER);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(146);
				match(STRING);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(147);
				match(IDENTIFIER);
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 5);
				{
				setState(148);
				match(TRUE);
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 6);
				{
				setState(149);
				match(FALSE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SignedNumberContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(PascalParser.INTEGER, 0); }
		public TerminalNode REAL() { return getToken(PascalParser.REAL, 0); }
		public TerminalNode PLUS() { return getToken(PascalParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(PascalParser.MINUS, 0); }
		public SignedNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signedNumber; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitSignedNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SignedNumberContext signedNumber() throws RecognitionException {
		SignedNumberContext _localctx = new SignedNumberContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_signedNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS || _la==MINUS) {
				{
				setState(152);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(155);
			_la = _input.LA(1);
			if ( !(_la==INTEGER || _la==REAL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeSectionContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(PascalParser.TYPE, 0); }
		public List<TypeDefinitionContext> typeDefinition() {
			return getRuleContexts(TypeDefinitionContext.class);
		}
		public TypeDefinitionContext typeDefinition(int i) {
			return getRuleContext(TypeDefinitionContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(PascalParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(PascalParser.SEMI, i);
		}
		public TypeSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitTypeSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSectionContext typeSection() throws RecognitionException {
		TypeSectionContext _localctx = new TypeSectionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_typeSection);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			match(TYPE);
			setState(158);
			typeDefinition();
			setState(163);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(159);
					match(SEMI);
					setState(160);
					typeDefinition();
					}
					} 
				}
				setState(165);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			setState(166);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode EQUAL() { return getToken(PascalParser.EQUAL, 0); }
		public TypeDenoterContext typeDenoter() {
			return getRuleContext(TypeDenoterContext.class,0);
		}
		public TypeDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitTypeDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDefinitionContext typeDefinition() throws RecognitionException {
		TypeDefinitionContext _localctx = new TypeDefinitionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_typeDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(IDENTIFIER);
			setState(169);
			match(EQUAL);
			setState(170);
			typeDenoter();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeDenoterContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode BOOLEAN() { return getToken(PascalParser.BOOLEAN, 0); }
		public TerminalNode ARRAY() { return getToken(PascalParser.ARRAY, 0); }
		public TerminalNode LBRACK() { return getToken(PascalParser.LBRACK, 0); }
		public IndexRangeContext indexRange() {
			return getRuleContext(IndexRangeContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(PascalParser.RBRACK, 0); }
		public TerminalNode OF() { return getToken(PascalParser.OF, 0); }
		public TypeDenoterContext typeDenoter() {
			return getRuleContext(TypeDenoterContext.class,0);
		}
		public TypeDenoterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDenoter; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitTypeDenoter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDenoterContext typeDenoter() throws RecognitionException {
		TypeDenoterContext _localctx = new TypeDenoterContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_typeDenoter);
		int _la;
		try {
			setState(180);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(172);
				_la = _input.LA(1);
				if ( !(_la==BOOLEAN || _la==IDENTIFIER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case ARRAY:
				enterOuterAlt(_localctx, 2);
				{
				setState(173);
				match(ARRAY);
				setState(174);
				match(LBRACK);
				setState(175);
				indexRange();
				setState(176);
				match(RBRACK);
				setState(177);
				match(OF);
				setState(178);
				typeDenoter();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IndexRangeContext extends ParserRuleContext {
		public List<SignedNumberContext> signedNumber() {
			return getRuleContexts(SignedNumberContext.class);
		}
		public SignedNumberContext signedNumber(int i) {
			return getRuleContext(SignedNumberContext.class,i);
		}
		public TerminalNode DOTDOT() { return getToken(PascalParser.DOTDOT, 0); }
		public IndexRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indexRange; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitIndexRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexRangeContext indexRange() throws RecognitionException {
		IndexRangeContext _localctx = new IndexRangeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_indexRange);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			signedNumber();
			setState(183);
			match(DOTDOT);
			setState(184);
			signedNumber();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarSectionContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(PascalParser.VAR, 0); }
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(PascalParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(PascalParser.SEMI, i);
		}
		public VarSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varSection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitVarSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarSectionContext varSection() throws RecognitionException {
		VarSectionContext _localctx = new VarSectionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_varSection);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(VAR);
			setState(187);
			varDeclaration();
			setState(192);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(188);
					match(SEMI);
					setState(189);
					varDeclaration();
					}
					} 
				}
				setState(194);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			setState(195);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclarationContext extends ParserRuleContext {
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public TerminalNode COLON() { return getToken(PascalParser.COLON, 0); }
		public TypeDenoterContext typeDenoter() {
			return getRuleContext(TypeDenoterContext.class,0);
		}
		public VarDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitVarDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclarationContext varDeclaration() throws RecognitionException {
		VarDeclarationContext _localctx = new VarDeclarationContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_varDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			identifierList();
			setState(198);
			match(COLON);
			setState(199);
			typeDenoter();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubroutineDeclarationPartContext extends ParserRuleContext {
		public ProcedureDeclarationContext procedureDeclaration() {
			return getRuleContext(ProcedureDeclarationContext.class,0);
		}
		public FunctionDeclarationContext functionDeclaration() {
			return getRuleContext(FunctionDeclarationContext.class,0);
		}
		public SubroutineDeclarationPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subroutineDeclarationPart; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitSubroutineDeclarationPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubroutineDeclarationPartContext subroutineDeclarationPart() throws RecognitionException {
		SubroutineDeclarationPartContext _localctx = new SubroutineDeclarationPartContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_subroutineDeclarationPart);
		try {
			setState(203);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PROCEDURE:
				enterOuterAlt(_localctx, 1);
				{
				setState(201);
				procedureDeclaration();
				}
				break;
			case FUNCTION:
				enterOuterAlt(_localctx, 2);
				{
				setState(202);
				functionDeclaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProcedureDeclarationContext extends ParserRuleContext {
		public TerminalNode PROCEDURE() { return getToken(PascalParser.PROCEDURE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public List<TerminalNode> SEMI() { return getTokens(PascalParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(PascalParser.SEMI, i);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(PascalParser.LPAREN, 0); }
		public FormalParameterListContext formalParameterList() {
			return getRuleContext(FormalParameterListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(PascalParser.RPAREN, 0); }
		public ProcedureDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_procedureDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitProcedureDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProcedureDeclarationContext procedureDeclaration() throws RecognitionException {
		ProcedureDeclarationContext _localctx = new ProcedureDeclarationContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_procedureDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(PROCEDURE);
			setState(206);
			match(IDENTIFIER);
			setState(211);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(207);
				match(LPAREN);
				setState(208);
				formalParameterList();
				setState(209);
				match(RPAREN);
				}
			}

			setState(213);
			match(SEMI);
			setState(214);
			block();
			setState(215);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDeclarationContext extends ParserRuleContext {
		public TerminalNode FUNCTION() { return getToken(PascalParser.FUNCTION, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(PascalParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(PascalParser.IDENTIFIER, i);
		}
		public TerminalNode COLON() { return getToken(PascalParser.COLON, 0); }
		public List<TerminalNode> SEMI() { return getTokens(PascalParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(PascalParser.SEMI, i);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(PascalParser.LPAREN, 0); }
		public FormalParameterListContext formalParameterList() {
			return getRuleContext(FormalParameterListContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(PascalParser.RPAREN, 0); }
		public FunctionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDeclaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclarationContext functionDeclaration() throws RecognitionException {
		FunctionDeclarationContext _localctx = new FunctionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_functionDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(217);
			match(FUNCTION);
			setState(218);
			match(IDENTIFIER);
			setState(223);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(219);
				match(LPAREN);
				setState(220);
				formalParameterList();
				setState(221);
				match(RPAREN);
				}
			}

			setState(225);
			match(COLON);
			setState(226);
			match(IDENTIFIER);
			setState(227);
			match(SEMI);
			setState(228);
			block();
			setState(229);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FormalParameterListContext extends ParserRuleContext {
		public List<FormalParameterSectionContext> formalParameterSection() {
			return getRuleContexts(FormalParameterSectionContext.class);
		}
		public FormalParameterSectionContext formalParameterSection(int i) {
			return getRuleContext(FormalParameterSectionContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(PascalParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(PascalParser.SEMI, i);
		}
		public FormalParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameterList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFormalParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalParameterListContext formalParameterList() throws RecognitionException {
		FormalParameterListContext _localctx = new FormalParameterListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_formalParameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			formalParameterSection();
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMI) {
				{
				{
				setState(232);
				match(SEMI);
				setState(233);
				formalParameterSection();
				}
				}
				setState(238);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FormalParameterSectionContext extends ParserRuleContext {
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public TerminalNode COLON() { return getToken(PascalParser.COLON, 0); }
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public FormalParameterSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParameterSection; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFormalParameterSection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalParameterSectionContext formalParameterSection() throws RecognitionException {
		FormalParameterSectionContext _localctx = new FormalParameterSectionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_formalParameterSection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			identifierList();
			setState(240);
			match(COLON);
			setState(241);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompoundStatementContext extends ParserRuleContext {
		public TerminalNode BEGIN() { return getToken(PascalParser.BEGIN, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public TerminalNode END() { return getToken(PascalParser.END, 0); }
		public CompoundStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compoundStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitCompoundStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompoundStatementContext compoundStatement() throws RecognitionException {
		CompoundStatementContext _localctx = new CompoundStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_compoundStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			match(BEGIN);
			setState(244);
			statementList();
			setState(245);
			match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementListContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(PascalParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(PascalParser.SEMI, i);
		}
		public StatementListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitStatementList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementListContext statementList() throws RecognitionException {
		StatementListContext _localctx = new StatementListContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_statementList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			statement();
			setState(252);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMI) {
				{
				{
				setState(248);
				match(SEMI);
				setState(249);
				statement();
				}
				}
				setState(254);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public AssignmentStatementContext assignmentStatement() {
			return getRuleContext(AssignmentStatementContext.class,0);
		}
		public ProcedureCallContext procedureCall() {
			return getRuleContext(ProcedureCallContext.class,0);
		}
		public IfStatementContext ifStatement() {
			return getRuleContext(IfStatementContext.class,0);
		}
		public WhileStatementContext whileStatement() {
			return getRuleContext(WhileStatementContext.class,0);
		}
		public EmptyStatementContext emptyStatement() {
			return getRuleContext(EmptyStatementContext.class,0);
		}
		public RepeatStatementContext repeatStatement() {
			return getRuleContext(RepeatStatementContext.class,0);
		}
		public ForStatementContext forStatement() {
			return getRuleContext(ForStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_statement);
		try {
			setState(263);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(255);
				compoundStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(256);
				assignmentStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(257);
				procedureCall();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(258);
				ifStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(259);
				whileStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(260);
				emptyStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(261);
				repeatStatement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(262);
				forStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignmentStatementContext extends ParserRuleContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(PascalParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignmentStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitAssignmentStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentStatementContext assignmentStatement() throws RecognitionException {
		AssignmentStatementContext _localctx = new AssignmentStatementContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_assignmentStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(265);
			variable();
			setState(266);
			match(ASSIGN);
			setState(267);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProcedureCallContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(PascalParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(PascalParser.RPAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ProcedureCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_procedureCall; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitProcedureCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProcedureCallContext procedureCall() throws RecognitionException {
		ProcedureCallContext _localctx = new ProcedureCallContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_procedureCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			match(IDENTIFIER);
			setState(275);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(270);
				match(LPAREN);
				setState(272);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 15)) & ~0x3f) == 0 && ((1L << (_la - 15)) & 17662555325464577L) != 0)) {
					{
					setState(271);
					expressionList();
					}
				}

				setState(274);
				match(RPAREN);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(PascalParser.IF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode THEN() { return getToken(PascalParser.THEN, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(PascalParser.ELSE, 0); }
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_ifStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			match(IF);
			setState(278);
			expression();
			setState(279);
			match(THEN);
			setState(280);
			statement();
			setState(283);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(281);
				match(ELSE);
				setState(282);
				statement();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(PascalParser.WHILE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DO() { return getToken(PascalParser.DO, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			match(WHILE);
			setState(286);
			expression();
			setState(287);
			match(DO);
			setState(288);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmptyStatementContext extends ParserRuleContext {
		public EmptyStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitEmptyStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyStatementContext emptyStatement() throws RecognitionException {
		EmptyStatementContext _localctx = new EmptyStatementContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_emptyStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public List<SimpleExpressionContext> simpleExpression() {
			return getRuleContexts(SimpleExpressionContext.class);
		}
		public SimpleExpressionContext simpleExpression(int i) {
			return getRuleContext(SimpleExpressionContext.class,i);
		}
		public TerminalNode EQUAL() { return getToken(PascalParser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(PascalParser.NOTEQUAL, 0); }
		public TerminalNode LESS() { return getToken(PascalParser.LESS, 0); }
		public TerminalNode GREATER() { return getToken(PascalParser.GREATER, 0); }
		public TerminalNode LESSEQUAL() { return getToken(PascalParser.LESSEQUAL, 0); }
		public TerminalNode GREATEREQUAL() { return getToken(PascalParser.GREATEREQUAL, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(292);
			simpleExpression();
			setState(295);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 917728L) != 0)) {
				{
				setState(293);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 917728L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(294);
				simpleExpression();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleExpressionContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(PascalParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(PascalParser.PLUS, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(PascalParser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(PascalParser.MINUS, i);
		}
		public List<TerminalNode> OR() { return getTokens(PascalParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(PascalParser.OR, i);
		}
		public SimpleExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleExpression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitSimpleExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleExpressionContext simpleExpression() throws RecognitionException {
		SimpleExpressionContext _localctx = new SimpleExpressionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_simpleExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			term();
			setState(302);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 70368744177670L) != 0)) {
				{
				{
				setState(298);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 70368744177670L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(299);
				term();
				}
				}
				setState(304);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TermContext extends ParserRuleContext {
		public List<FactorContext> factor() {
			return getRuleContexts(FactorContext.class);
		}
		public FactorContext factor(int i) {
			return getRuleContext(FactorContext.class,i);
		}
		public List<TerminalNode> STAR() { return getTokens(PascalParser.STAR); }
		public TerminalNode STAR(int i) {
			return getToken(PascalParser.STAR, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(PascalParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(PascalParser.SLASH, i);
		}
		public List<TerminalNode> DIV() { return getTokens(PascalParser.DIV); }
		public TerminalNode DIV(int i) {
			return getToken(PascalParser.DIV, i);
		}
		public List<TerminalNode> MOD() { return getTokens(PascalParser.MOD); }
		public TerminalNode MOD(int i) {
			return getToken(PascalParser.MOD, i);
		}
		public List<TerminalNode> AND() { return getTokens(PascalParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(PascalParser.AND, i);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			factor();
			setState(310);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4399153807384L) != 0)) {
				{
				{
				setState(306);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4399153807384L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(307);
				factor();
				}
				}
				setState(312);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FactorContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(PascalParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(PascalParser.RPAREN, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode INTEGER() { return getToken(PascalParser.INTEGER, 0); }
		public TerminalNode REAL() { return getToken(PascalParser.REAL, 0); }
		public TerminalNode CHARACTER() { return getToken(PascalParser.CHARACTER, 0); }
		public TerminalNode STRING() { return getToken(PascalParser.STRING, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode NOT() { return getToken(PascalParser.NOT, 0); }
		public FactorContext factor() {
			return getRuleContext(FactorContext.class,0);
		}
		public TerminalNode TRUE() { return getToken(PascalParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(PascalParser.FALSE, 0); }
		public FactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFactor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactorContext factor() throws RecognitionException {
		FactorContext _localctx = new FactorContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_factor);
		int _la;
		try {
			setState(333);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(313);
				match(IDENTIFIER);
				setState(319);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==LPAREN) {
					{
					setState(314);
					match(LPAREN);
					setState(316);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (((((_la - 15)) & ~0x3f) == 0 && ((1L << (_la - 15)) & 17662555325464577L) != 0)) {
						{
						setState(315);
						expressionList();
						}
					}

					setState(318);
					match(RPAREN);
					}
				}

				}
				break;
			case INTEGER:
				enterOuterAlt(_localctx, 2);
				{
				setState(321);
				match(INTEGER);
				}
				break;
			case REAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(322);
				match(REAL);
				}
				break;
			case CHARACTER:
				enterOuterAlt(_localctx, 4);
				{
				setState(323);
				match(CHARACTER);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 5);
				{
				setState(324);
				match(STRING);
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 6);
				{
				setState(325);
				match(LPAREN);
				setState(326);
				expression();
				setState(327);
				match(RPAREN);
				}
				break;
			case NOT:
				enterOuterAlt(_localctx, 7);
				{
				setState(329);
				match(NOT);
				setState(330);
				factor();
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 8);
				{
				setState(331);
				match(TRUE);
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 9);
				{
				setState(332);
				match(FALSE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(PascalParser.IDENTIFIER, 0); }
		public TerminalNode LBRACK() { return getToken(PascalParser.LBRACK, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(PascalParser.RBRACK, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_variable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(IDENTIFIER);
			setState(340);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBRACK) {
				{
				setState(336);
				match(LBRACK);
				setState(337);
				expression();
				setState(338);
				match(RBRACK);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionItemContext> expressionItem() {
			return getRuleContexts(ExpressionItemContext.class);
		}
		public ExpressionItemContext expressionItem(int i) {
			return getRuleContext(ExpressionItemContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PascalParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PascalParser.COMMA, i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(342);
			expressionItem();
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(343);
				match(COMMA);
				setState(344);
				expressionItem();
				}
				}
				setState(349);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionItemContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public FormattedExpressionContext formattedExpression() {
			return getRuleContext(FormattedExpressionContext.class,0);
		}
		public ExpressionItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionItem; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitExpressionItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionItemContext expressionItem() throws RecognitionException {
		ExpressionItemContext _localctx = new ExpressionItemContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_expressionItem);
		try {
			setState(352);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(350);
				expression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(351);
				formattedExpression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FormattedExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<TerminalNode> COLON() { return getTokens(PascalParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(PascalParser.COLON, i);
		}
		public List<TerminalNode> INTEGER() { return getTokens(PascalParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(PascalParser.INTEGER, i);
		}
		public FormattedExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formattedExpression; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitFormattedExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormattedExpressionContext formattedExpression() throws RecognitionException {
		FormattedExpressionContext _localctx = new FormattedExpressionContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_formattedExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(354);
			expression();
			setState(355);
			match(COLON);
			setState(356);
			match(INTEGER);
			setState(357);
			match(COLON);
			setState(358);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RepeatStatementContext extends ParserRuleContext {
		public TerminalNode REPEAT() { return getToken(PascalParser.REPEAT, 0); }
		public StatementListContext statementList() {
			return getRuleContext(StatementListContext.class,0);
		}
		public TerminalNode UNTIL() { return getToken(PascalParser.UNTIL, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public RepeatStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_repeatStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitRepeatStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RepeatStatementContext repeatStatement() throws RecognitionException {
		RepeatStatementContext _localctx = new RepeatStatementContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_repeatStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(360);
			match(REPEAT);
			setState(361);
			statementList();
			setState(362);
			match(UNTIL);
			setState(363);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(PascalParser.FOR, 0); }
		public AssignmentStatementContext assignmentStatement() {
			return getRuleContext(AssignmentStatementContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DO() { return getToken(PascalParser.DO, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode TO() { return getToken(PascalParser.TO, 0); }
		public TerminalNode DOWNTO() { return getToken(PascalParser.DOWNTO, 0); }
		public ForStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStatement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PascalParserVisitor ) return ((PascalParserVisitor<? extends T>)visitor).visitForStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForStatementContext forStatement() throws RecognitionException {
		ForStatementContext _localctx = new ForStatementContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_forStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(365);
			match(FOR);
			setState(366);
			assignmentStatement();
			setState(367);
			_la = _input.LA(1);
			if ( !(_la==DOWNTO || _la==TO) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(368);
			expression();
			setState(369);
			match(DO);
			setState(370);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001F\u0175\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000S\b\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0005\u0001\\\b\u0001\n\u0001\f\u0001_\t\u0001\u0001\u0002\u0003\u0002"+
		"b\b\u0002\u0001\u0002\u0003\u0002e\b\u0002\u0001\u0002\u0003\u0002h\b"+
		"\u0002\u0001\u0002\u0003\u0002k\b\u0002\u0001\u0002\u0005\u0002n\b\u0002"+
		"\n\u0002\f\u0002q\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0005\u0003y\b\u0003\n\u0003\f\u0003|\t\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0005\u0005\u0086\b\u0005\n\u0005\f\u0005\u0089"+
		"\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0003\u0007\u0097\b\u0007\u0001\b\u0003\b\u009a\b\b\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\t\u0001\t\u0005\t\u00a2\b\t\n\t\f\t\u00a5\t\t"+
		"\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u00b5\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0005\r\u00bf\b\r\n\r\f\r\u00c2\t\r\u0001\r\u0001\r"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f"+
		"\u0003\u000f\u00cc\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0003\u0010\u00d4\b\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0003\u0011\u00e0\b\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0005\u0012\u00eb\b\u0012\n\u0012\f\u0012\u00ee\t\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u00fb"+
		"\b\u0015\n\u0015\f\u0015\u00fe\t\u0015\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016"+
		"\u0108\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0003\u0018\u0111\b\u0018\u0001\u0018\u0003\u0018"+
		"\u0114\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0003\u0019\u011c\b\u0019\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0003\u001c\u0128\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0005\u001d\u012d\b\u001d\n\u001d\f\u001d\u0130\t\u001d\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0005\u001e\u0135\b\u001e\n\u001e\f\u001e\u0138\t\u001e"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u013d\b\u001f\u0001\u001f"+
		"\u0003\u001f\u0140\b\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0003\u001f\u014e\b\u001f\u0001 \u0001 \u0001"+
		" \u0001 \u0001 \u0003 \u0155\b \u0001!\u0001!\u0001!\u0005!\u015a\b!\n"+
		"!\f!\u015d\t!\u0001\"\u0001\"\u0003\"\u0161\b\"\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0001#\u0001$\u0001$\u0001$\u0001$\u0001$\u0001%\u0001%\u0001"+
		"%\u0001%\u0001%\u0001%\u0001%\u0001%\u0000\u0000&\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,."+
		"02468:<>@BDFHJ\u0000\u0007\u0001\u0000\u0001\u0002\u0001\u0000@A\u0002"+
		"\u0000<<DD\u0002\u0000\u0005\u0007\u0011\u0013\u0002\u0000\u0001\u0002"+
		"..\u0004\u0000\u0003\u0004\u0019\u0019\u001e\u001e**\u0002\u0000  66\u017f"+
		"\u0000L\u0001\u0000\u0000\u0000\u0002X\u0001\u0000\u0000\u0000\u0004a"+
		"\u0001\u0000\u0000\u0000\u0006t\u0001\u0000\u0000\u0000\b\u007f\u0001"+
		"\u0000\u0000\u0000\n\u0081\u0001\u0000\u0000\u0000\f\u008c\u0001\u0000"+
		"\u0000\u0000\u000e\u0096\u0001\u0000\u0000\u0000\u0010\u0099\u0001\u0000"+
		"\u0000\u0000\u0012\u009d\u0001\u0000\u0000\u0000\u0014\u00a8\u0001\u0000"+
		"\u0000\u0000\u0016\u00b4\u0001\u0000\u0000\u0000\u0018\u00b6\u0001\u0000"+
		"\u0000\u0000\u001a\u00ba\u0001\u0000\u0000\u0000\u001c\u00c5\u0001\u0000"+
		"\u0000\u0000\u001e\u00cb\u0001\u0000\u0000\u0000 \u00cd\u0001\u0000\u0000"+
		"\u0000\"\u00d9\u0001\u0000\u0000\u0000$\u00e7\u0001\u0000\u0000\u0000"+
		"&\u00ef\u0001\u0000\u0000\u0000(\u00f3\u0001\u0000\u0000\u0000*\u00f7"+
		"\u0001\u0000\u0000\u0000,\u0107\u0001\u0000\u0000\u0000.\u0109\u0001\u0000"+
		"\u0000\u00000\u010d\u0001\u0000\u0000\u00002\u0115\u0001\u0000\u0000\u0000"+
		"4\u011d\u0001\u0000\u0000\u00006\u0122\u0001\u0000\u0000\u00008\u0124"+
		"\u0001\u0000\u0000\u0000:\u0129\u0001\u0000\u0000\u0000<\u0131\u0001\u0000"+
		"\u0000\u0000>\u014d\u0001\u0000\u0000\u0000@\u014f\u0001\u0000\u0000\u0000"+
		"B\u0156\u0001\u0000\u0000\u0000D\u0160\u0001\u0000\u0000\u0000F\u0162"+
		"\u0001\u0000\u0000\u0000H\u0168\u0001\u0000\u0000\u0000J\u016d\u0001\u0000"+
		"\u0000\u0000LM\u00051\u0000\u0000MR\u0005D\u0000\u0000NO\u0005\u000f\u0000"+
		"\u0000OP\u0003\u0002\u0001\u0000PQ\u0005\u0010\u0000\u0000QS\u0001\u0000"+
		"\u0000\u0000RN\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000ST\u0001"+
		"\u0000\u0000\u0000TU\u0005\r\u0000\u0000UV\u0003\u0004\u0002\u0000VW\u0005"+
		"\n\u0000\u0000W\u0001\u0001\u0000\u0000\u0000X]\u0005D\u0000\u0000YZ\u0005"+
		"\u000b\u0000\u0000Z\\\u0005D\u0000\u0000[Y\u0001\u0000\u0000\u0000\\_"+
		"\u0001\u0000\u0000\u0000][\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000"+
		"\u0000^\u0003\u0001\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000`b\u0003"+
		"\u0006\u0003\u0000a`\u0001\u0000\u0000\u0000ab\u0001\u0000\u0000\u0000"+
		"bd\u0001\u0000\u0000\u0000ce\u0003\n\u0005\u0000dc\u0001\u0000\u0000\u0000"+
		"de\u0001\u0000\u0000\u0000eg\u0001\u0000\u0000\u0000fh\u0003\u0012\t\u0000"+
		"gf\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000hj\u0001\u0000\u0000"+
		"\u0000ik\u0003\u001a\r\u0000ji\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000"+
		"\u0000ko\u0001\u0000\u0000\u0000ln\u0003\u001e\u000f\u0000ml\u0001\u0000"+
		"\u0000\u0000nq\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000op\u0001"+
		"\u0000\u0000\u0000pr\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000"+
		"rs\u0003(\u0014\u0000s\u0005\u0001\u0000\u0000\u0000tu\u0005)\u0000\u0000"+
		"uz\u0003\b\u0004\u0000vw\u0005\u000b\u0000\u0000wy\u0003\b\u0004\u0000"+
		"xv\u0001\u0000\u0000\u0000y|\u0001\u0000\u0000\u0000zx\u0001\u0000\u0000"+
		"\u0000z{\u0001\u0000\u0000\u0000{}\u0001\u0000\u0000\u0000|z\u0001\u0000"+
		"\u0000\u0000}~\u0005\r\u0000\u0000~\u0007\u0001\u0000\u0000\u0000\u007f"+
		"\u0080\u0005@\u0000\u0000\u0080\t\u0001\u0000\u0000\u0000\u0081\u0082"+
		"\u0005\u001d\u0000\u0000\u0082\u0087\u0003\f\u0006\u0000\u0083\u0084\u0005"+
		"\r\u0000\u0000\u0084\u0086\u0003\f\u0006\u0000\u0085\u0083\u0001\u0000"+
		"\u0000\u0000\u0086\u0089\u0001\u0000\u0000\u0000\u0087\u0085\u0001\u0000"+
		"\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088\u008a\u0001\u0000"+
		"\u0000\u0000\u0089\u0087\u0001\u0000\u0000\u0000\u008a\u008b\u0005\r\u0000"+
		"\u0000\u008b\u000b\u0001\u0000\u0000\u0000\u008c\u008d\u0005D\u0000\u0000"+
		"\u008d\u008e\u0005\u0005\u0000\u0000\u008e\u008f\u0003\u000e\u0007\u0000"+
		"\u008f\r\u0001\u0000\u0000\u0000\u0090\u0097\u0003\u0010\b\u0000\u0091"+
		"\u0097\u0005B\u0000\u0000\u0092\u0097\u0005C\u0000\u0000\u0093\u0097\u0005"+
		"D\u0000\u0000\u0094\u0097\u0005=\u0000\u0000\u0095\u0097\u0005>\u0000"+
		"\u0000\u0096\u0090\u0001\u0000\u0000\u0000\u0096\u0091\u0001\u0000\u0000"+
		"\u0000\u0096\u0092\u0001\u0000\u0000\u0000\u0096\u0093\u0001\u0000\u0000"+
		"\u0000\u0096\u0094\u0001\u0000\u0000\u0000\u0096\u0095\u0001\u0000\u0000"+
		"\u0000\u0097\u000f\u0001\u0000\u0000\u0000\u0098\u009a\u0007\u0000\u0000"+
		"\u0000\u0099\u0098\u0001\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000"+
		"\u0000\u009a\u009b\u0001\u0000\u0000\u0000\u009b\u009c\u0007\u0001\u0000"+
		"\u0000\u009c\u0011\u0001\u0000\u0000\u0000\u009d\u009e\u00057\u0000\u0000"+
		"\u009e\u00a3\u0003\u0014\n\u0000\u009f\u00a0\u0005\r\u0000\u0000\u00a0"+
		"\u00a2\u0003\u0014\n\u0000\u00a1\u009f\u0001\u0000\u0000\u0000\u00a2\u00a5"+
		"\u0001\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a3\u00a4"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a6\u0001\u0000\u0000\u0000\u00a5\u00a3"+
		"\u0001\u0000\u0000\u0000\u00a6\u00a7\u0005\r\u0000\u0000\u00a7\u0013\u0001"+
		"\u0000\u0000\u0000\u00a8\u00a9\u0005D\u0000\u0000\u00a9\u00aa\u0005\u0005"+
		"\u0000\u0000\u00aa\u00ab\u0003\u0016\u000b\u0000\u00ab\u0015\u0001\u0000"+
		"\u0000\u0000\u00ac\u00b5\u0007\u0002\u0000\u0000\u00ad\u00ae\u0005\u001a"+
		"\u0000\u0000\u00ae\u00af\u0005\b\u0000\u0000\u00af\u00b0\u0003\u0018\f"+
		"\u0000\u00b0\u00b1\u0005\t\u0000\u0000\u00b1\u00b2\u0005-\u0000\u0000"+
		"\u00b2\u00b3\u0003\u0016\u000b\u0000\u00b3\u00b5\u0001\u0000\u0000\u0000"+
		"\u00b4\u00ac\u0001\u0000\u0000\u0000\u00b4\u00ad\u0001\u0000\u0000\u0000"+
		"\u00b5\u0017\u0001\u0000\u0000\u0000\u00b6\u00b7\u0003\u0010\b\u0000\u00b7"+
		"\u00b8\u0005\u0015\u0000\u0000\u00b8\u00b9\u0003\u0010\b\u0000\u00b9\u0019"+
		"\u0001\u0000\u0000\u0000\u00ba\u00bb\u00059\u0000\u0000\u00bb\u00c0\u0003"+
		"\u001c\u000e\u0000\u00bc\u00bd\u0005\r\u0000\u0000\u00bd\u00bf\u0003\u001c"+
		"\u000e\u0000\u00be\u00bc\u0001\u0000\u0000\u0000\u00bf\u00c2\u0001\u0000"+
		"\u0000\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c0\u00c1\u0001\u0000"+
		"\u0000\u0000\u00c1\u00c3\u0001\u0000\u0000\u0000\u00c2\u00c0\u0001\u0000"+
		"\u0000\u0000\u00c3\u00c4\u0005\r\u0000\u0000\u00c4\u001b\u0001\u0000\u0000"+
		"\u0000\u00c5\u00c6\u0003\u0002\u0001\u0000\u00c6\u00c7\u0005\f\u0000\u0000"+
		"\u00c7\u00c8\u0003\u0016\u000b\u0000\u00c8\u001d\u0001\u0000\u0000\u0000"+
		"\u00c9\u00cc\u0003 \u0010\u0000\u00ca\u00cc\u0003\"\u0011\u0000\u00cb"+
		"\u00c9\u0001\u0000\u0000\u0000\u00cb\u00ca\u0001\u0000\u0000\u0000\u00cc"+
		"\u001f\u0001\u0000\u0000\u0000\u00cd\u00ce\u00050\u0000\u0000\u00ce\u00d3"+
		"\u0005D\u0000\u0000\u00cf\u00d0\u0005\u000f\u0000\u0000\u00d0\u00d1\u0003"+
		"$\u0012\u0000\u00d1\u00d2\u0005\u0010\u0000\u0000\u00d2\u00d4\u0001\u0000"+
		"\u0000\u0000\u00d3\u00cf\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000"+
		"\u0000\u0000\u00d4\u00d5\u0001\u0000\u0000\u0000\u00d5\u00d6\u0005\r\u0000"+
		"\u0000\u00d6\u00d7\u0003\u0004\u0002\u0000\u00d7\u00d8\u0005\r\u0000\u0000"+
		"\u00d8!\u0001\u0000\u0000\u0000\u00d9\u00da\u0005%\u0000\u0000\u00da\u00df"+
		"\u0005D\u0000\u0000\u00db\u00dc\u0005\u000f\u0000\u0000\u00dc\u00dd\u0003"+
		"$\u0012\u0000\u00dd\u00de\u0005\u0010\u0000\u0000\u00de\u00e0\u0001\u0000"+
		"\u0000\u0000\u00df\u00db\u0001\u0000\u0000\u0000\u00df\u00e0\u0001\u0000"+
		"\u0000\u0000\u00e0\u00e1\u0001\u0000\u0000\u0000\u00e1\u00e2\u0005\f\u0000"+
		"\u0000\u00e2\u00e3\u0005D\u0000\u0000\u00e3\u00e4\u0005\r\u0000\u0000"+
		"\u00e4\u00e5\u0003\u0004\u0002\u0000\u00e5\u00e6\u0005\r\u0000\u0000\u00e6"+
		"#\u0001\u0000\u0000\u0000\u00e7\u00ec\u0003&\u0013\u0000\u00e8\u00e9\u0005"+
		"\r\u0000\u0000\u00e9\u00eb\u0003&\u0013\u0000\u00ea\u00e8\u0001\u0000"+
		"\u0000\u0000\u00eb\u00ee\u0001\u0000\u0000\u0000\u00ec\u00ea\u0001\u0000"+
		"\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed%\u0001\u0000\u0000"+
		"\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ef\u00f0\u0003\u0002\u0001"+
		"\u0000\u00f0\u00f1\u0005\f\u0000\u0000\u00f1\u00f2\u0005D\u0000\u0000"+
		"\u00f2\'\u0001\u0000\u0000\u0000\u00f3\u00f4\u0005\u001b\u0000\u0000\u00f4"+
		"\u00f5\u0003*\u0015\u0000\u00f5\u00f6\u0005\"\u0000\u0000\u00f6)\u0001"+
		"\u0000\u0000\u0000\u00f7\u00fc\u0003,\u0016\u0000\u00f8\u00f9\u0005\r"+
		"\u0000\u0000\u00f9\u00fb\u0003,\u0016\u0000\u00fa\u00f8\u0001\u0000\u0000"+
		"\u0000\u00fb\u00fe\u0001\u0000\u0000\u0000\u00fc\u00fa\u0001\u0000\u0000"+
		"\u0000\u00fc\u00fd\u0001\u0000\u0000\u0000\u00fd+\u0001\u0000\u0000\u0000"+
		"\u00fe\u00fc\u0001\u0000\u0000\u0000\u00ff\u0108\u0003(\u0014\u0000\u0100"+
		"\u0108\u0003.\u0017\u0000\u0101\u0108\u00030\u0018\u0000\u0102\u0108\u0003"+
		"2\u0019\u0000\u0103\u0108\u00034\u001a\u0000\u0104\u0108\u00036\u001b"+
		"\u0000\u0105\u0108\u0003H$\u0000\u0106\u0108\u0003J%\u0000\u0107\u00ff"+
		"\u0001\u0000\u0000\u0000\u0107\u0100\u0001\u0000\u0000\u0000\u0107\u0101"+
		"\u0001\u0000\u0000\u0000\u0107\u0102\u0001\u0000\u0000\u0000\u0107\u0103"+
		"\u0001\u0000\u0000\u0000\u0107\u0104\u0001\u0000\u0000\u0000\u0107\u0105"+
		"\u0001\u0000\u0000\u0000\u0107\u0106\u0001\u0000\u0000\u0000\u0108-\u0001"+
		"\u0000\u0000\u0000\u0109\u010a\u0003@ \u0000\u010a\u010b\u0005\u0014\u0000"+
		"\u0000\u010b\u010c\u00038\u001c\u0000\u010c/\u0001\u0000\u0000\u0000\u010d"+
		"\u0113\u0005D\u0000\u0000\u010e\u0110\u0005\u000f\u0000\u0000\u010f\u0111"+
		"\u0003B!\u0000\u0110\u010f\u0001\u0000\u0000\u0000\u0110\u0111\u0001\u0000"+
		"\u0000\u0000\u0111\u0112\u0001\u0000\u0000\u0000\u0112\u0114\u0005\u0010"+
		"\u0000\u0000\u0113\u010e\u0001\u0000\u0000\u0000\u0113\u0114\u0001\u0000"+
		"\u0000\u0000\u01141\u0001\u0000\u0000\u0000\u0115\u0116\u0005\'\u0000"+
		"\u0000\u0116\u0117\u00038\u001c\u0000\u0117\u0118\u00055\u0000\u0000\u0118"+
		"\u011b\u0003,\u0016\u0000\u0119\u011a\u0005!\u0000\u0000\u011a\u011c\u0003"+
		",\u0016\u0000\u011b\u0119\u0001\u0000\u0000\u0000\u011b\u011c\u0001\u0000"+
		"\u0000\u0000\u011c3\u0001\u0000\u0000\u0000\u011d\u011e\u0005:\u0000\u0000"+
		"\u011e\u011f\u00038\u001c\u0000\u011f\u0120\u0005\u001f\u0000\u0000\u0120"+
		"\u0121\u0003,\u0016\u0000\u01215\u0001\u0000\u0000\u0000\u0122\u0123\u0001"+
		"\u0000\u0000\u0000\u01237\u0001\u0000\u0000\u0000\u0124\u0127\u0003:\u001d"+
		"\u0000\u0125\u0126\u0007\u0003\u0000\u0000\u0126\u0128\u0003:\u001d\u0000"+
		"\u0127\u0125\u0001\u0000\u0000\u0000\u0127\u0128\u0001\u0000\u0000\u0000"+
		"\u01289\u0001\u0000\u0000\u0000\u0129\u012e\u0003<\u001e\u0000\u012a\u012b"+
		"\u0007\u0004\u0000\u0000\u012b\u012d\u0003<\u001e\u0000\u012c\u012a\u0001"+
		"\u0000\u0000\u0000\u012d\u0130\u0001\u0000\u0000\u0000\u012e\u012c\u0001"+
		"\u0000\u0000\u0000\u012e\u012f\u0001\u0000\u0000\u0000\u012f;\u0001\u0000"+
		"\u0000\u0000\u0130\u012e\u0001\u0000\u0000\u0000\u0131\u0136\u0003>\u001f"+
		"\u0000\u0132\u0133\u0007\u0005\u0000\u0000\u0133\u0135\u0003>\u001f\u0000"+
		"\u0134\u0132\u0001\u0000\u0000\u0000\u0135\u0138\u0001\u0000\u0000\u0000"+
		"\u0136\u0134\u0001\u0000\u0000\u0000\u0136\u0137\u0001\u0000\u0000\u0000"+
		"\u0137=\u0001\u0000\u0000\u0000\u0138\u0136\u0001\u0000\u0000\u0000\u0139"+
		"\u013f\u0005D\u0000\u0000\u013a\u013c\u0005\u000f\u0000\u0000\u013b\u013d"+
		"\u0003B!\u0000\u013c\u013b\u0001\u0000\u0000\u0000\u013c\u013d\u0001\u0000"+
		"\u0000\u0000\u013d\u013e\u0001\u0000\u0000\u0000\u013e\u0140\u0005\u0010"+
		"\u0000\u0000\u013f\u013a\u0001\u0000\u0000\u0000\u013f\u0140\u0001\u0000"+
		"\u0000\u0000\u0140\u014e\u0001\u0000\u0000\u0000\u0141\u014e\u0005@\u0000"+
		"\u0000\u0142\u014e\u0005A\u0000\u0000\u0143\u014e\u0005B\u0000\u0000\u0144"+
		"\u014e\u0005C\u0000\u0000\u0145\u0146\u0005\u000f\u0000\u0000\u0146\u0147"+
		"\u00038\u001c\u0000\u0147\u0148\u0005\u0010\u0000\u0000\u0148\u014e\u0001"+
		"\u0000\u0000\u0000\u0149\u014a\u0005,\u0000\u0000\u014a\u014e\u0003>\u001f"+
		"\u0000\u014b\u014e\u0005=\u0000\u0000\u014c\u014e\u0005>\u0000\u0000\u014d"+
		"\u0139\u0001\u0000\u0000\u0000\u014d\u0141\u0001\u0000\u0000\u0000\u014d"+
		"\u0142\u0001\u0000\u0000\u0000\u014d\u0143\u0001\u0000\u0000\u0000\u014d"+
		"\u0144\u0001\u0000\u0000\u0000\u014d\u0145\u0001\u0000\u0000\u0000\u014d"+
		"\u0149\u0001\u0000\u0000\u0000\u014d\u014b\u0001\u0000\u0000\u0000\u014d"+
		"\u014c\u0001\u0000\u0000\u0000\u014e?\u0001\u0000\u0000\u0000\u014f\u0154"+
		"\u0005D\u0000\u0000\u0150\u0151\u0005\b\u0000\u0000\u0151\u0152\u0003"+
		"8\u001c\u0000\u0152\u0153\u0005\t\u0000\u0000\u0153\u0155\u0001\u0000"+
		"\u0000\u0000\u0154\u0150\u0001\u0000\u0000\u0000\u0154\u0155\u0001\u0000"+
		"\u0000\u0000\u0155A\u0001\u0000\u0000\u0000\u0156\u015b\u0003D\"\u0000"+
		"\u0157\u0158\u0005\u000b\u0000\u0000\u0158\u015a\u0003D\"\u0000\u0159"+
		"\u0157\u0001\u0000\u0000\u0000\u015a\u015d\u0001\u0000\u0000\u0000\u015b"+
		"\u0159\u0001\u0000\u0000\u0000\u015b\u015c\u0001\u0000\u0000\u0000\u015c"+
		"C\u0001\u0000\u0000\u0000\u015d\u015b\u0001\u0000\u0000\u0000\u015e\u0161"+
		"\u00038\u001c\u0000\u015f\u0161\u0003F#\u0000\u0160\u015e\u0001\u0000"+
		"\u0000\u0000\u0160\u015f\u0001\u0000\u0000\u0000\u0161E\u0001\u0000\u0000"+
		"\u0000\u0162\u0163\u00038\u001c\u0000\u0163\u0164\u0005\f\u0000\u0000"+
		"\u0164\u0165\u0005@\u0000\u0000\u0165\u0166\u0005\f\u0000\u0000\u0166"+
		"\u0167\u0005@\u0000\u0000\u0167G\u0001\u0000\u0000\u0000\u0168\u0169\u0005"+
		"3\u0000\u0000\u0169\u016a\u0003*\u0015\u0000\u016a\u016b\u00058\u0000"+
		"\u0000\u016b\u016c\u00038\u001c\u0000\u016cI\u0001\u0000\u0000\u0000\u016d"+
		"\u016e\u0005$\u0000\u0000\u016e\u016f\u0003.\u0017\u0000\u016f\u0170\u0007"+
		"\u0006\u0000\u0000\u0170\u0171\u00038\u001c\u0000\u0171\u0172\u0005\u001f"+
		"\u0000\u0000\u0172\u0173\u0003,\u0016\u0000\u0173K\u0001\u0000\u0000\u0000"+
		" R]adgjoz\u0087\u0096\u0099\u00a3\u00b4\u00c0\u00cb\u00d3\u00df\u00ec"+
		"\u00fc\u0107\u0110\u0113\u011b\u0127\u012e\u0136\u013c\u013f\u014d\u0154"+
		"\u015b\u0160";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}