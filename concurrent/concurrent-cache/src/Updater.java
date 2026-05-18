/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 10.03.2026 11:49
 */
public interface Updater {
    //Интерфейс ч/з который пользователи обновляют кэш
    void updateCurrentState(long[] currentCacheState);
}
