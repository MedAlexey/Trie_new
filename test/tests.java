import org.junit.Test;
import org.junit.Assert;

public class tests {

    Trie trie = new Trie();

    @Test
    public void addAndFind() {

        trie.add("слон");
        Assert.assertTrue(trie.find("слон"));

        trie.add("слово");
        Assert.assertTrue(trie.find("слово"));

        trie.add("словарь");
        Assert.assertTrue(trie.find("словарь"));

        trie.add("ТАКТИКА");
        Assert.assertTrue(trie.find("Тактика"));

        trie.add("танк");
        Assert.assertTrue(trie.find("танк"));
    }

    @Test
    public void delete(){

        trie.add("слон");
        trie.add("слово");
        trie.add("словарь");
        trie.add("ТАКТИКА");
        trie.add("танк");

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
}
