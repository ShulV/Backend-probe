/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 10.03.2026 11:47
 */
public interface Cache {
    //Метод для обновления кэша через мутацию
    void bulkUpdate();

    //Метод который принимает индексы для чтения
    long[] bulkRead(int[] indices);
}
