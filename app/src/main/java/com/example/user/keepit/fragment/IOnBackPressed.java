package com.example.user.keepit.fragment;

/**
 * Solution found on stackoverflow.com ( https://stackoverflow.com/questions/5448653/how-to-implement-onbackpressed-in-fragments#46425415)
 */
public interface IOnBackPressed  {
    /**
     * If you return true the back press will not be taken into account, otherwise the activity will
     * act naturally
     * @return true if your processing has priority if not false
     */
    boolean onBackPressed();
}
