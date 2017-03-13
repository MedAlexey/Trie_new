import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Trie {

    static class TrieNode {
        Character info;      // очередная буква
        TrieNode son;        // указатель на следующий уровень
        TrieNode brother;    //указатель на следующий узел того же уровня
        TrieNode father;     //отец
        boolean endOfWord;   //является ли концом слова
        int level;           //на каком уровне расположен


        //конструктор узла
        public TrieNode(Character info,
                        TrieNode son,
                        TrieNode brother,
                        TrieNode father,
                        boolean endOfWord,
                        int level) {
            this.info = info;
            this.son = son;
            this.brother = brother;
            this.father = father;
            this.endOfWord = false;
            this.level = level;
        }

        boolean hasBrother(){
            if (this.brother != null) return true;
            else return false;
        }

        boolean hasSon(){
            if (this.son != null) return true;
            else return false;
        }
    }


    private ArrayList<ArrayList<TrieNode>> mainTrie = new ArrayList<ArrayList<TrieNode>>(); //дерево


    public void add(String string) {  //добавление

        /** если в дереве нет даже корня, то создаём корень
         */
        if (mainTrie.size() == 0) {
            ArrayList<TrieNode> firstLvl = new ArrayList<TrieNode>();
            TrieNode root = new TrieNode(null, null, null, null, false, 0);  //корень
            firstLvl.add(root);
            mainTrie.add(firstLvl);
        }


        TrieNode curNode = mainTrie.get(0).get(0);  //узел, в котором мы находимся
        string = string.toLowerCase();

        for (int i = 0; i < string.length(); i++) {
            Character nextChar = new Character(string.charAt(i)); //берём следующую букву

            if (curNode.level == 0) {
                if (!curNode.hasSon()) {
                    ArrayList<TrieNode> arr = new ArrayList<TrieNode>();
                    TrieNode son = new TrieNode(nextChar, null, null, curNode, false, curNode.level + 1);
                    if (i == string.length() - 1) son.endOfWord = true;
                    arr.add(son);
                    mainTrie.add(arr);
                    curNode.son = mainTrie.get(curNode.level + 1).get(mainTrie.get(curNode.level + 1).size() - 1);
                }
                curNode = curNode.son;
            }


            while (!curNode.info.equals(nextChar) && curNode.hasBrother()) {  //идём по одному уровню
                curNode = curNode.brother;
            }


            if (!curNode.info.equals(nextChar) && !curNode.hasBrother()) { //если дошли до конца уровня и не нашли букву, то добарляем брата
                TrieNode brother = new TrieNode(nextChar, null, null, curNode.father, false, curNode.level);
                if (i == string.length() - 1) brother.endOfWord = true;
                mainTrie.get(curNode.level).add(brother);
                curNode.brother = mainTrie.get(curNode.level).get(mainTrie.get(curNode.level).size() - 1); //прикрепляем брата
                curNode = curNode.brother;
            } else if (curNode.info.equals(nextChar) && (i == string.length() - 1)) { //если нашли букву и она последняя в слове
                curNode.endOfWord = true;
            }

            if (curNode.hasSon()) curNode = curNode.son;       //переходим на след уровень
            else if (i != string.length() - 1 && !curNode.hasSon()) {
                Character letter = new Character(string.charAt(i + 1));   //следующая буква слова
                TrieNode son = new TrieNode(letter, null, null, curNode, false, curNode.level + 1);

                if (mainTrie.size() > curNode.level + 1) mainTrie.get(curNode.level + 1).add(son);
                else if (mainTrie.size() <= curNode.level + 1) {
                    ArrayList<TrieNode> arr = new ArrayList<TrieNode>();
                    arr.add(son);
                    mainTrie.add(arr);
                }

                curNode.son = mainTrie.get(curNode.level + 1).get(mainTrie.get(curNode.level + 1).size() - 1);
                curNode = curNode.son;

            }
        }

    }


    public boolean find(String string) {
        if (mainTrie.size() == 0) return false;

        TrieNode curNode = mainTrie.get(0).get(0);   //начинаем с вершины
        string = string.toLowerCase();

        for (int i = 0; i < string.length(); i++) {
            Character nextChar = new Character(string.charAt(i));
            if (curNode.hasSon()) curNode = curNode.son;                 //переходим на нижний уровень
            else return false;

            while (!curNode.info.equals(nextChar) && curNode.hasBrother()) {   //пока не найдём нужную букву в уровне
                curNode = curNode.brother;
            }

            if (!curNode.info.equals(nextChar)) return false;   //если дошли до конца уровня и не нашли след букву
            if ((i == string.length() - 1) && !curNode.endOfWord)
                return false;  //если нашли,но поледняя буква не последняя
        }
        return true;
    }


    public void delete(String string) {
        TrieNode curNode = mainTrie.get(0).get(0);   //начальное положение
        string = string.toLowerCase();

        if (!find(string)) throw new IllegalArgumentException("Такого слова нет.");

        for (int i = 0; i < string.length(); i++) {    //доходим до последней буквы слова
            Character nextChar = new Character(string.charAt(i));
            curNode = curNode.son;
            while (curNode.hasBrother() && !curNode.info.equals(nextChar)) {
                curNode = curNode.brother;
            }
        }

        for (int i = string.length(); i > 0; i--){              //идём вверх по уровням, удаляя буквы
            if (!curNode.hasBrother() && !curNode.hasSon()) {     //если буква правая крайняя на уровне
                curNode = curNode.father;
                if(!curNode.son.hasBrother()){      // если сын единственный
                    curNode.son.info = '-';
                    curNode.son = null;
                }
                else {                              //если сын не единственный
                    TrieNode tmpCurNode = curNode;
                    tmpCurNode = tmpCurNode.son;
                    while (tmpCurNode.brother.hasBrother()) {   //доходим до предпоследнего брата
                        tmpCurNode = tmpCurNode.brother;
                    }
                    tmpCurNode.brother.info = '-';
                    tmpCurNode.brother = null;
                }
            }
            else if (curNode.hasSon() && curNode.endOfWord){
                curNode.endOfWord = false;
                curNode = curNode.father;
            }
            else if (!curNode.hasSon() && curNode.hasBrother()){
                curNode.info = '-';
                curNode.endOfWord = false;
                curNode = curNode.father;
            }

            //удаляем ненужные крайние элементы на уровнях
            while(mainTrie.get(i).size() > 0 && mainTrie.get(i).get(mainTrie.get(i).size()-1).info == '-'){
                mainTrie.get(i).remove(mainTrie.get(i).size()-1);
            }
            if (mainTrie.get(i).size() == 0) mainTrie.remove(i);  //если уровень пустой, удаляем

        }
    }

    public List<String> findStrings(String prefix){     //поиск по префиксу
        TrieNode curNode = mainTrie.get(0).get(0);
        prefix = prefix.toLowerCase();

        if (!prefixExist(prefix)) throw new IllegalArgumentException("Дерево не содержит такого префикса.");

        for (int i = 0; i < prefix.length(); i++){      //доходим до последней буквы слова
            Character letter = new Character(prefix.charAt(i));
            curNode = curNode.son;
            while(!curNode.info.equals(letter) && curNode.hasBrother()) curNode = curNode.brother;
        }

        ArrayList<String> collector = new ArrayList<String>(); //собирает строкис таким префиксом

        if (curNode.endOfWord) {
            collector.add(prefix);
            return collector;   //если префикс является словом
        }

        String result = prefix;

        addLetter(curNode,result,collector);
        return collector;

    }

    private void addLetter(TrieNode curNode, String result, ArrayList<String> collector){

        if (curNode.hasSon()){
            curNode = curNode.son;
            if (curNode.info != '-') result = result + curNode.info;
            if (curNode.endOfWord) collector.add(result);
            
            addLetter(curNode, result, collector);
        }

        while (curNode.hasBrother()){
            curNode = curNode.brother;
            if (curNode.info != '-') {
                result = result.substring(0,result.length()-1);
                result = result + curNode.info;
            }
            if (curNode.endOfWord) collector.add(result);
            addLetter(curNode, result, collector);
        }
    }


    private boolean prefixExist(String prefix){

        if (mainTrie.size() == 0) return false;

        TrieNode curNode = mainTrie.get(0).get(0);   //начинаем с вершины

        for (int i = 0; i < prefix.length(); i++) {
            Character letter = new Character(prefix.charAt(i));
            if (curNode.hasSon()) curNode = curNode.son;                 //переходим на нижний уровень
            else return false;

            while (!curNode.info.equals(letter) && curNode.hasBrother()) {   //пока не найдём нужную букву в уровне
                curNode = curNode.brother;
            }

            if (!curNode.info.equals(letter)) return false;   //если дошли до конца уровня и не нашли след букву
        }
        return true;
    }

}
