// tables/SymbolTable.java
package tables;

import entries.Entry;
import typing.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Formatter;

public class SymbolTable {
    
    // Lista de escopos ativos - cada escopo é um mapa de nome para Entry
    private List<Map<String, Entry>> activeScopes;
    
    // Lista de nomes dos escopos ativos
    private List<String> activeScopeNames;
    
    // HISTÓRICO - para manter escopos que já foram fechados
    private List<ScopeHistory> scopeHistory;
    
    // Contador de escopos anônimos
    private int anonymousScopeCount = 0;
    
    // Classe para armazenar histórico de um escopo
    private static class ScopeHistory {
        String scopeName;
        int level;
        Map<String, Entry> entries;
        
        ScopeHistory(String scopeName, int level, Map<String, Entry> entries) {
            this.scopeName = scopeName;
            this.level = level;
            this.entries = new HashMap<>(entries); // Copia as entries
        }
    }
    
    public SymbolTable() {
        activeScopes = new ArrayList<>();
        activeScopeNames = new ArrayList<>();
        scopeHistory = new ArrayList<>();
        // Cria escopo global
        openScope("GLOBAL");
    }
    
    /**
     * Abre um novo escopo
     */
    public void openScope(String scopeName) {
        activeScopes.add(new HashMap<>());
        activeScopeNames.add(scopeName);
    }
    
    /**
     * Abre um escopo anônimo (para blocos)
     */
    public void openScope() {
        openScope("BLOCK_" + (++anonymousScopeCount));
    }
    
    /**
     * Fecha o escopo atual e salva no histórico (não pode fechar o escopo global)
     */
    public void closeScope() {
        if (activeScopes.size() > 1) {
            // Salva no histórico antes de fechar
            String scopeName = activeScopeNames.get(activeScopeNames.size() - 1);
            int level = activeScopes.size() - 1;
            Map<String, Entry> entries = activeScopes.get(activeScopes.size() - 1);
            
            scopeHistory.add(new ScopeHistory(scopeName, level, entries));
            
            // Remove da lista ativa
            activeScopes.remove(activeScopes.size() - 1);
            activeScopeNames.remove(activeScopeNames.size() - 1);
        }
    }
    
    /**
     * Adiciona uma entrada no escopo atual
     */
    public boolean addEntry(String name, Entry entry) {
        Map<String, Entry> currentScope = getCurrentScope();
        
        // Verifica se já existe no escopo atual
        if (currentScope.containsKey(name)) {
            return false; // Já existe
        }
        
        currentScope.put(name, entry);
        
        return true;
    }
    
    /**
     * Busca uma entrada em todos os escopos ativos (do atual para o global)
     */
    public Entry lookupEntry(String name) {
        // Busca do escopo mais interno para o mais externo
        for (int i = activeScopes.size() - 1; i >= 0; i--) {
            Entry entry = activeScopes.get(i).get(name);
            if (entry != null) {
                return entry;
            }
        }
        return null; // Não encontrado
    }
    
    /**
     * Busca uma entrada apenas no escopo atual
     */
    public Entry lookupCurrentScope(String name) {
        return getCurrentScope().get(name);
    }
    
    /**
     * Retorna o escopo atual
     */
    private Map<String, Entry> getCurrentScope() {
        return activeScopes.get(activeScopes.size() - 1);
    }
    
    /**
     * Retorna o nome do escopo atual
     */
    public String getCurrentScopeName() {
        return activeScopeNames.get(activeScopeNames.size() - 1);
    }
    
    /**
     * Retorna o nível do escopo atual (0 = global)
     */
    public int getCurrentScopeLevel() {
        return activeScopes.size() - 1;
    }
    
    /**
     * Print da tabela incluindo escopos ativos E histórico
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        
        f.format("=== SYMBOL TABLE ===\n");
        
        // First, print all scope history (closed scopes)
        for (ScopeHistory history : scopeHistory) {
            f.format("\n--- SCOPE: %s (Level %d) [CLOSED] ---\n", history.scopeName, history.level);
            
            if (history.entries.isEmpty()) {
                f.format("  (empty scope)\n");
            } else {
                int entryIndex = 0;
                for (Map.Entry<String, Entry> mapEntry : history.entries.entrySet()) {
                    String name = mapEntry.getKey();
                    Entry entry = mapEntry.getValue();
                    
                    f.format("  Entry %d -- name: %s, line: %d, type: %s, class: %s\n", 
                            entryIndex++, name, entry.getLine(), 
                            entry.getEntryType().toString(), 
                            entry.getClass().getSimpleName());
                }
            }
        }
        
        // Then print active scopes
        for (int scopeIndex = 0; scopeIndex < activeScopes.size(); scopeIndex++) {
            String scopeName = activeScopeNames.get(scopeIndex);
            Map<String, Entry> scope = activeScopes.get(scopeIndex);
            
            f.format("\n--- SCOPE: %s (Level %d) [ACTIVE] ---\n", scopeName, scopeIndex);
            
            if (scope.isEmpty()) {
                f.format("  (empty scope)\n");
            } else {
                int entryIndex = 0;
                for (Map.Entry<String, Entry> mapEntry : scope.entrySet()) {
                    String name = mapEntry.getKey();
                    Entry entry = mapEntry.getValue();
                    
                    f.format("  Entry %d -- name: %s, line: %d, type: %s, class: %s\n", 
                            entryIndex++, name, entry.getLine(), 
                            entry.getEntryType().toString(), 
                            entry.getClass().getSimpleName());
                }
            }
        }
        
        f.format("\n=== END SYMBOL TABLE ===\n");
        f.close();
        return sb.toString();
    }
}