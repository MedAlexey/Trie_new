import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class tests {

    private Trie trie = new Trie();

    @Before
    public void add(){
        trie.add("king");
        trie.add("know");
        trie.add("crocodile");
        trie.add("knife");
        trie.add("star");
        trie.add("transport");
        trie.add("string");
        trie.add("stranger");
        trie.add("starter");
        trie.add("street");
        trie.add("train");

        trie.add("слон");
        trie.add("слово");
        trie.add("словарь");
        trie.add("ТАКТИКА");
        trie.add("танк");
        trie.add("слоны");
    }


    @Test
    public void addAndFind() {

        Assert.assertTrue(trie.find("слоны"));
        Assert.assertTrue(trie.find("слон"));
        Assert.assertTrue(trie.find("слово"));
        Assert.assertTrue(trie.find("словарь"));
        Assert.assertTrue(trie.find("Тактика"));
        Assert.assertTrue(trie.find("танк"));
        Assert.assertFalse(trie.find("катер"));
    }


    @Test
    public void delete() throws IllegalArgumentException{

        trie.delete("слово");
        Assert.assertTrue(trie.find("слон"));
        Assert.assertFalse(trie.find("слово"));
        Assert.assertTrue(trie.find("словарь"));
        Assert.assertTrue(trie.find("Тактика"));
        Assert.assertTrue(trie.find("танк"));

        trie.delete("танк");
        Assert.assertTrue(trie.find("слон"));
        Assert.assertFalse(trie.find("слово"));
        Assert.assertTrue(trie.find("словарь"));
        Assert.assertTrue(trie.find("Тактика"));
        Assert.assertFalse(trie.find("танк"));

        trie.delete("словарь");
        Assert.assertTrue(trie.find("слон"));
        Assert.assertFalse(trie.find("слово"));
        Assert.assertFalse(trie.find("словарь"));
        Assert.assertTrue(trie.find("Тактика"));
        Assert.assertFalse(trie.find("танк"));

        trie.delete("слон");
        Assert.assertFalse(trie.find("слон"));
        Assert.assertFalse(trie.find("слово"));
        Assert.assertFalse(trie.find("словарь"));
        Assert.assertTrue(trie.find("Тактика"));
        Assert.assertFalse(trie.find("танк"));

        trie.delete("ТАктИка");
        Assert.assertFalse(trie.find("слон"));
        Assert.assertFalse(trie.find("слово"));
        Assert.assertFalse(trie.find("словарь"));
        Assert.assertFalse(trie.find("Тактика"));
        Assert.assertFalse(trie.find("танк"));
    }


    @Test
    public void findStrings(){

        ArrayList<String> arr = new ArrayList<String>();
        arr.add("know");
        Assert.assertEquals(arr, trie.findStrings("kno"));

        arr.clear();
        arr.add("king");
        arr.add("know");
        arr.add("knife");
        Assert.assertEquals(arr, trie.findStrings("k"));

        arr.clear();
        arr.add("train");
        Assert.assertEquals(arr,trie.findStrings("train"));

    }
}
