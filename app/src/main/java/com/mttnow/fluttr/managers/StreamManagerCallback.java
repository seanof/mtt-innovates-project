package com.mttnow.fluttr.managers;

import java.util.List;

/**
 * Created by seanb on 23/11/2016.
 */

interface StreamManagerCallback <T> {
  void streamReady(List<T> res);
}
