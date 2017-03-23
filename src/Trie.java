import java.util.ArrayList;
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


    private TrieNode root = new TrieNode(null,null,null,null,false,0);

    public void add(String string) {  //добавление

        TrieNode curNode = root; //начинаем с вершины
        string = string.toLowerCase();

        for (int i = 0; i < string.length(); i++) {
            char nextChar = string.charAt(i);// берём следующую букву

          if (curNode.level == 0){ //озможно можно заменить на ...== null
              if (!curNode.hasSon()){
                  TrieNode son = new TrieNode(nextChar,null,null, curNode,false,curNode.level+1);
                  if (i == string.length() - 1) son.endOfWord = true;
                  curNode.son = son;
              }
              curNode = curNode.son;
          }


            while (!(curNode.info == nextChar) && curNode.hasBrother()) {  //идём по одному уровню
                curNode = curNode.brother;
            }


            if (!(curNode.info == nextChar) && !curNode.hasBrother()) { //если дошли до конца уровня и не нашли букву, то добарляем брата

                TrieNode brother = new TrieNode(nextChar,null,null, curNode.father,false,curNode.level);
                if (i == string.length() -1 ) brother.endOfWord = true;
                curNode.brother = brother;
                curNode = curNode.brother;
            } else if (curNode.info == nextChar && (i == string.length() - 1)) { //если нашли букву и она последняя в слове
                curNode.endOfWord = true;
            }

            if (curNode.hasSon()) curNode = curNode.son;       //переходим на след уровень
            else if (i != string.length() - 1 && !curNode.hasSon()) {  //если нет сына, то добавляем его
                char letter = string.charAt(i+1); // следующая буква слова
                TrieNode son = new TrieNode(letter, null, null, curNode, false, curNode.level + 1);

                curNode.son = son;
                curNode = curNode.son;

            }
        }

    }


    public boolean find(String string) {

        TrieNode curNode = root;
        string = string.toLowerCase();

        for (int i = 0; i < string.length(); i++) {
            char nextChar = string.charAt(i);
            if (curNode.hasSon()) curNode = curNode.son;                 //переходим на нижний уровень
            else return false;

            while (!(curNode.info == nextChar) && curNode.hasBrother()) {   //пока не найдём нужную букву в уровне
                curNode = curNode.brother;
            }

            if (!(curNode.info == nextChar)) return false;   //если дошли до конца уровня и не нашли след букву
            if ((i == string.length() - 1) && !curNode.endOfWord)
                return false;  //если нашли,но поледняя буква не последняя
        }
        return true;
    }


    public void delete(String string) throws IllegalArgumentException {

        TrieNode curNode = root;
        string = string.toLowerCase();

        if (!find(string)) throw new IllegalArgumentException("Такого слова нет.");

        for (int i = 0; i < string.length(); i++) {    //доходим до последней буквы слова
            char nextChar = string.charAt(i);
            curNode = curNode.son;
            while (curNode.hasBrother() && !(curNode.info == nextChar)) {
                curNode = curNode.brother;
            }
        }

        for (int i = string.length(); i > 0; i--){              //идём вверх по уровням, удаляя буквы
            if (!curNode.hasBrother() && !curNode.hasSon()) {     //если буква правая крайняя на уровне
                curNode = curNode.father;
                if(!curNode.son.hasBrother()){      // если сын единственный
                    curNode.son = null;
                }
                else {                              //если сын не единственный
                    TrieNode tmpCurNode = curNode;
                    tmpCurNode = tmpCurNode.son;
                    while (tmpCurNode.brother.hasBrother()) {   //доходим до предпоследнего брата
                        tmpCurNode = tmpCurNode.brother;
                    }
                    tmpCurNode.brother = null;
                }
            }
            else if (curNode.hasSon() && curNode.endOfWord){
                curNode.endOfWord = false;
                curNode = curNode.father;
            }
            else if (!curNode.father.son.info.equals(curNode.info) && !curNode.hasSon() && curNode.hasBrother()){ //если не кайняя левая на уровне

                TrieNode tmpCurNode = curNode.father.son;
                while(!tmpCurNode.brother.info.equals(curNode.info)){
                    tmpCurNode = tmpCurNode.brother;
                }
                tmpCurNode.brother = tmpCurNode.brother.brother;
                curNode = curNode.father;
            }
            else if (curNode.father.son.info.equals(curNode.info) && !curNode.hasSon() && curNode.hasBrother()){
                curNode = curNode.father;
                curNode.son = curNode.son.brother;
            }
            else if (curNode.hasSon()) curNode = curNode.father;

        }
    }

    public List<String> findStrings(String prefix) throws IllegalArgumentException{     //поиск по префиксу

        TrieNode curNode = root;
        prefix = prefix.toLowerCase();

        if (!prefixExist(prefix)) throw new IllegalArgumentException("Дерево не содержит такого префикса.");

        for (int i = 0; i < prefix.length(); i++){      //доходим до последней буквы слова
            char letter = prefix.charAt(i);
            curNode = curNode.son;
            while(!(curNode.info == letter) && curNode.hasBrother()) curNode = curNode.brother;
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
            result = result + curNode.info;
            if (curNode.endOfWord) collector.add(result);
            
            addLetter(curNode, result, collector);
        }

        while (curNode.hasBrother()){
            curNode = curNode.brother;
            result = result.substring(0,result.length()-1);
            result = result + curNode.info;

            if (curNode.endOfWord) collector.add(result);
            addLetter(curNode, result, collector);
        }
    }


    private boolean prefixExist(String prefix){

        TrieNode curNode = root;   //начинаем с вершины

        for (int i = 0; i < prefix.length(); i++) {
            char letter = prefix.charAt(i);
            if (curNode.hasSon()) curNode = curNode.son;                 //переходим на нижний уровень
            else return false;

            while (!(curNode.info == letter) && curNode.hasBrother()) {   //пока не найдём нужную букву в уровне
                curNode = curNode.brother;
            }

            if (!(curNode.info == letter)) return false;   //если дошли до конца уровня и не нашли след букву
        }
        return true;
    }

}
