// Generated from /home/yomaia/Trab_Compiladores-main/PascalParser.g by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PascalParser}.
 */
public interface PascalParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PascalParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(PascalParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(PascalParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(PascalParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(PascalParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(PascalParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(PascalParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#labelSection}.
	 * @param ctx the parse tree
	 */
	void enterLabelSection(PascalParser.LabelSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#labelSection}.
	 * @param ctx the parse tree
	 */
	void exitLabelSection(PascalParser.LabelSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(PascalParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(PascalParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#constSection}.
	 * @param ctx the parse tree
	 */
	void enterConstSection(PascalParser.ConstSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#constSection}.
	 * @param ctx the parse tree
	 */
	void exitConstSection(PascalParser.ConstSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#constDefinition}.
	 * @param ctx the parse tree
	 */
	void enterConstDefinition(PascalParser.ConstDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#constDefinition}.
	 * @param ctx the parse tree
	 */
	void exitConstDefinition(PascalParser.ConstDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(PascalParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(PascalParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#signedNumber}.
	 * @param ctx the parse tree
	 */
	void enterSignedNumber(PascalParser.SignedNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#signedNumber}.
	 * @param ctx the parse tree
	 */
	void exitSignedNumber(PascalParser.SignedNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#typeSection}.
	 * @param ctx the parse tree
	 */
	void enterTypeSection(PascalParser.TypeSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#typeSection}.
	 * @param ctx the parse tree
	 */
	void exitTypeSection(PascalParser.TypeSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTypeDefinition(PascalParser.TypeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#typeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTypeDefinition(PascalParser.TypeDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#typeDenoter}.
	 * @param ctx the parse tree
	 */
	void enterTypeDenoter(PascalParser.TypeDenoterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#typeDenoter}.
	 * @param ctx the parse tree
	 */
	void exitTypeDenoter(PascalParser.TypeDenoterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#indexRange}.
	 * @param ctx the parse tree
	 */
	void enterIndexRange(PascalParser.IndexRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#indexRange}.
	 * @param ctx the parse tree
	 */
	void exitIndexRange(PascalParser.IndexRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#varSection}.
	 * @param ctx the parse tree
	 */
	void enterVarSection(PascalParser.VarSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#varSection}.
	 * @param ctx the parse tree
	 */
	void exitVarSection(PascalParser.VarSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(PascalParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(PascalParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#subroutineDeclarationPart}.
	 * @param ctx the parse tree
	 */
	void enterSubroutineDeclarationPart(PascalParser.SubroutineDeclarationPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#subroutineDeclarationPart}.
	 * @param ctx the parse tree
	 */
	void exitSubroutineDeclarationPart(PascalParser.SubroutineDeclarationPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#procedureDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterProcedureDeclaration(PascalParser.ProcedureDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#procedureDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitProcedureDeclaration(PascalParser.ProcedureDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(PascalParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(PascalParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#formalParameterSection}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterSection(PascalParser.FormalParameterSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#formalParameterSection}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterSection(PascalParser.FormalParameterSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(PascalParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(PascalParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(PascalParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(PascalParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(PascalParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(PascalParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStatement(PascalParser.AssignmentStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStatement(PascalParser.AssignmentStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#procedureCall}.
	 * @param ctx the parse tree
	 */
	void enterProcedureCall(PascalParser.ProcedureCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#procedureCall}.
	 * @param ctx the parse tree
	 */
	void exitProcedureCall(PascalParser.ProcedureCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(PascalParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(PascalParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(PascalParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(PascalParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(PascalParser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(PascalParser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(PascalParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(PascalParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleExpression(PascalParser.SimpleExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleExpression(PascalParser.SimpleExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(PascalParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(PascalParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(PascalParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(PascalParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(PascalParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(PascalParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(PascalParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(PascalParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#expressionItem}.
	 * @param ctx the parse tree
	 */
	void enterExpressionItem(PascalParser.ExpressionItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#expressionItem}.
	 * @param ctx the parse tree
	 */
	void exitExpressionItem(PascalParser.ExpressionItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#formattedExpression}.
	 * @param ctx the parse tree
	 */
	void enterFormattedExpression(PascalParser.FormattedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#formattedExpression}.
	 * @param ctx the parse tree
	 */
	void exitFormattedExpression(PascalParser.FormattedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void enterRepeatStatement(PascalParser.RepeatStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#repeatStatement}.
	 * @param ctx the parse tree
	 */
	void exitRepeatStatement(PascalParser.RepeatStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PascalParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(PascalParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PascalParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(PascalParser.ForStatementContext ctx);
}