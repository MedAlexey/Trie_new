import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Trie {

    static class TrieNode {
        Object info;         // очередная буква или связанный объект
        TrieNode son;        // указатель на следующий уровень
        TrieNode brother;    //указатель на следующий узел того же уровня
        TrieNode father;     //отец
        boolean hasBrother;  //есть ли брат
        boolean hasSon;      //есть ли сын
        boolean endOfWord;   //является ли концом слова
        int level;           //на каком уровне расположен


        //конструктор узла
        public TrieNode(Object info,
                        TrieNode son,
                        TrieNode brother,
                        TrieNode father,
                        boolean hasBrother,
                        boolean hasSon,
                        boolean endOfWord,
                        int level) {
            this.info = info;
            this.son = son;
            this.brother = brother;
            this.father = father;
            this.hasBrother = false;
            this.hasSon = false;
            this.endOfWord = false;
            this.level = level;
        }
    }


    ArrayList<ArrayList<TrieNode>> mainTrie = new ArrayList<ArrayList<TrieNode>>(); //дерево


    public void add(String string) {  //добавление

        /** сли в дереве нет даже корня, то создаём корень
         */
        if (mainTrie.size() == 0) {
            ArrayList<TrieNode> firstLvl = new ArrayList<TrieNode>();
            TrieNode root = new TrieNode(null, null, null, null, false, false, false, 0);  //корень
            firstLvl.add(root);
            mainTrie.add(firstLvl);
        }

        /** начальное положение в корне
         */
        TrieNode curNode = mainTrie.get(0).get(0);  //узел, в котором мы находимся
        string = string.toLowerCase();

        for (int i = 0; i < string.length(); i++) {
            Character nextChar = new Character(string.charAt(i)); //берём следующую букву

            if (curNode.level == 0) {
                if (!curNode.hasSon) {
                    ArrayList<TrieNode> arr = new ArrayList<TrieNode>();
                    TrieNode son = new TrieNode(nextChar, null, null, curNode, false, false, false, curNode.level + 1);
                    if (i == string.length() - 1) son.endOfWord = true;
                    arr.add(son);
                    mainTrie.add(arr);
                    curNode.son = mainTrie.get(curNode.level + 1).get(mainTrie.get(curNode.level + 1).size() - 1);
                    curNode.hasSon = true;
                }
                curNode = curNode.son;
            }


            while (!curNode.info.equals(nextChar) && curNode.hasBrother) {  //идём по одному уровню
                curNode = curNode.brother;
            }


            if (!curNode.info.equals(nextChar) && !curNode.hasBrother) { //если дошли до конца уровня и не нашли букву, то добарляем брата
                TrieNode brother = new TrieNode(nextChar, null, null, curNode.father, false, false, false, curNode.level);
                if (i == string.length() - 1) brother.endOfWord = true;
                mainTrie.get(curNode.level).add(brother);
                curNode.brother = mainTrie.get(curNode.level).get(mainTrie.get(curNode.level).size() - 1); //прикрепляем брата
                curNode.hasBrother = true;
                curNode = curNode.brother;
            } else if (curNode.info.equals(nextChar) && (i == string.length() - 1)) { //если нашли букву и она последняя в слове
                curNode.endOfWord = true;
            }

            if (curNode.hasSon) curNode = curNode.son;       //переходим на след уровень
            else if (i != string.length() - 1 && !curNode.hasSon) {
                Character letter = new Character(string.charAt(i + 1));   //следующая буква слова
                TrieNode son = new TrieNode(letter, null, null, curNode, false, false, false, curNode.level + 1);

                if (mainTrie.size() > curNode.level + 1) mainTrie.get(curNode.level + 1).add(son);
                else if (mainTrie.size() <= curNode.level + 1) {
                    ArrayList<TrieNode> arr = new ArrayList<TrieNode>();
                    arr.add(son);
                    mainTrie.add(arr);
                }

                curNode.son = mainTrie.get(curNode.level + 1).get(mainTrie.get(curNode.level + 1).size() - 1);
                curNode.hasSon = true;
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
            if (curNode.hasSon) curNode = curNode.son;                 //переходим на нижний уровень
            else return false;

            while (!curNode.info.equals(nextChar) && curNode.hasBrother) {   //пока не найдём нужную букву в уровне
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

        if (!find(string)) System.out.print("Такого слова нет");

        for (int i = 0; i < string.length(); i++) {    //доходим до последней буквы слова
            Character nextChar = new Character(string.charAt(i));
            curNode = curNode.son;
            while (curNode.hasBrother && !curNode.info.equals(nextChar)) {
                curNode = curNode.brother;
            }
        }

        for (int i = string.length(); i > 0; i--){              //идём вверх по уровням, удаляя буквы
            if (!curNode.hasBrother && !curNode.hasSon) {     //если буква правая крайняя на уровне
                curNode = curNode.father;
                if(!curNode.son.hasBrother){      // если сын единственный
                    curNode.son.info = "-";
                    curNode.son = null;
                    curNode.hasSon = false;
                }
                else {                              //если сын не единственный
                    TrieNode tmpCurNode = curNode;
                    tmpCurNode = tmpCurNode.son;
                    while (tmpCurNode.brother.hasBrother) {   //доходим до предпоследнего брата
                        tmpCurNode = tmpCurNode.brother;
                    }
                    tmpCurNode.brother.info = "-";
                    tmpCurNode.brother = null;
                    tmpCurNode.hasBrother = false;
                }
            }
            else if (curNode.hasSon && curNode.endOfWord){
                curNode.endOfWord = false;
                curNode = curNode.father;
            }
            else if (!curNode.hasSon && curNode.hasBrother){
                curNode.info = '-';
                curNode.endOfWord = false;
                curNode = curNode.father;
            }

            //удаляем ненужные крайние элементы на уровнях
            while(mainTrie.get(i).size() > 0 && mainTrie.get(i).get(mainTrie.get(i).size()-1).info == "-"){
                mainTrie.get(i).remove(mainTrie.get(i).size()-1);
            }
            if (mainTrie.get(i).size() == 0) mainTrie.remove(i);  //если уровень пустой, удаляем

        }
    }

    public void findStrings(String prefix){
        TrieNode curNode = mainTrie.get(0).get(0);

        for (int i = 0; i < prefix.length(); i++){      //доходим до последней буквы слова
            Character letter = new Character(prefix.charAt(i));
            curNode = curNode.son;
            while(!curNode.info.equals(letter) && curNode.hasBrother) curNode = curNode.brother;
        }

        //StringBuilder result = new StringBuilder(prefix);
        String result = prefix;

        addLetter(curNode,result);

        /**
         * спуститься вниз дерева по буквам префикса
         *
         * пройтись( рекурсивно) по всем ветвям этой буквы, собирая слова и выводя их(наткнувшись на последнюю букву)
         */
    }


    /**
     * со строкой
     * @param curNode
     * @param result
     */
    private void addLetter(TrieNode curNode, String result){
        if (curNode.hasSon){
            //curNode = curNode.son;
            if (curNode.son.hasSon || curNode.son.endOfWord) result = result + curNode.son.info.toString();
            if (curNode.son.endOfWord) System.out.println(result);
            addLetter(curNode.son,result);
        }


        while(curNode.hasBrother) {
            //curNode = curNode.brother;
            if (curNode.brother.hasSon || curNode.brother.endOfWord){
                result = result.substring(0,result.length()-1);
                result = result + curNode.brother.info.toString();
            }
            addLetter(curNode.brother, result);
        }
       /* for (int i = 0; i < mainTrie.get(curNode.level).size(); i++){
           if (curNode.hasBrother) {
               curNode = curNode.brother;
               result = result.substring(0,result.length()-1);
               if (curNode.hasSon || curNode.endOfWord){
                   result = result + curNode.info.toString();
               }
               addLetter(curNode, result);
           }
        }*/
    }


    /**
     * со стрингбилдером
     */
/*    private void addLetter(TrieNode curNode, StringBuilder builder){
        if (curNode.hasSon){
            curNode = curNode.son;
            if (curNode.hasSon || curNode.endOfWord) builder.append(curNode.info);
            if (curNode.endOfWord) {
                String result = builder.toString();
                System.out.println(result);
            }
            addLetter(curNode, builder);
        }

        while(curNode.hasBrother){
            curNode = curNode.brother;
            builder.deleteCharAt(builder.length()-1);
            if (curNode.hasSon || curNode.endOfWord){
                builder.append(curNode.info);
            }
            addLetter(curNode, builder);
        }
        /*for (int i = 0; i < mainTrie.get(curNode.level).size()-1; i++){
            if (curNode.hasBrother){
                curNode = curNode.brother;
                if (curNode.hasSon || curNode.endOfWord){
                    builder.deleteCharAt(builder.length()-1);
                    builder.append(curNode.info);
                }
                addLetter(curNode,builder);
            }
        }
    }*/
}
