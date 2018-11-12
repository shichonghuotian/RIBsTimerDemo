package com.gogovan.test.app;

import com.gogovan.test.app.history.HistoryBuilder;
import com.gogovan.test.app.history.HistoryInteractor;
import com.gogovan.test.app.history.HistoryRouter;
import com.gogovan.test.app.history.HistoryView;
import com.uber.rib.core.RibTestBasePlaceholder;
import com.uber.rib.core.RouterHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HistoryRouterTest extends RibTestBasePlaceholder {

  @Mock
  HistoryBuilder.Component component;
  @Mock
  HistoryInteractor interactor;
  @Mock
  HistoryView view;

  private HistoryRouter router;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    router = new HistoryRouter(view, interactor, component);
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  public void anExampleTest_withSomeConditions_shouldPass() {
    // Use RouterHelper to drive your router's lifecycle.
    RouterHelper.attach(router);
    RouterHelper.detach(router);

    throw new RuntimeException("Remove this test and add real tests.");
  }

}
