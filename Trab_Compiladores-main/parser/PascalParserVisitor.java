// Generated from PascalParser.g by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PascalParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PascalParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PascalParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(PascalParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(PascalParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(PascalParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#labelSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelSection(PascalParser.LabelSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(PascalParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#constSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstSection(PascalParser.ConstSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#constDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDefinition(PascalParser.ConstDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(PascalParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#signedNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedNumber(PascalParser.SignedNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#typeSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSection(PascalParser.TypeSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#typeDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDefinition(PascalParser.TypeDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#typeDenoter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDenoter(PascalParser.TypeDenoterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#indexRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexRange(PascalParser.IndexRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#varSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarSection(PascalParser.VarSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(PascalParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#subroutineDeclarationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubroutineDeclarationPart(PascalParser.SubroutineDeclarationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#procedureDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureDeclaration(PascalParser.ProcedureDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#functionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(PascalParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#formalParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterList(PascalParser.FormalParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#formalParameterSection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterSection(PascalParser.FormalParameterSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(PascalParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#statementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementList(PascalParser.StatementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(PascalParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#assignmentStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStatement(PascalParser.AssignmentStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#procedureCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureCall(PascalParser.ProcedureCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(PascalParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(PascalParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#emptyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStatement(PascalParser.EmptyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(PascalParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#simpleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleExpression(PascalParser.SimpleExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(PascalParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(PascalParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(PascalParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(PascalParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#expressionItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionItem(PascalParser.ExpressionItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#formattedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormattedExpression(PascalParser.FormattedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#repeatStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeatStatement(PascalParser.RepeatStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PascalParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(PascalParser.ForStatementContext ctx);
}