package dev.xhyrom.lighteco.common.model.chat;

public abstract class AbstractCommandSender<T> implements CommandSender {
  protected final T delegate;

  protected AbstractCommandSender(T delegate) {
      this.delegate = delegate;
  }
}
