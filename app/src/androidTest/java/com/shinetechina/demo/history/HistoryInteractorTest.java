package com.shinetechina.demo.history;

import com.uber.rib.core.RibTestBasePlaceholder;
import com.uber.rib.core.InteractorHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HistoryInteractorTest extends RibTestBasePlaceholder {

  @Mock HistoryInteractor.HistoryPresenter presenter;
  @Mock HistoryRouter router;

  private HistoryInteractor interactor;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    interactor = TestHistoryInteractor.create(presenter);
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  public void anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach(interactor, presenter, router, null);
    InteractorHelper.detach(interactor);

    throw new RuntimeException("Remove this test and add real tests.");
  }

}
