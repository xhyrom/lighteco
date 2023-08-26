package dev.xhyrom.lighteco.common.model.chat;

public abstract class AbstractCommandSender<T> implements CommandSender {
  protected final T delegate;

  public AbstractCommandSender(T delegate) {
      this.delegate = delegate;
  }
}
