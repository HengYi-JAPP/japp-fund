export interface ConfirmOption {
  title?: string;
  textContent?: string;
  okText?: string;
  cancelText?: string;
  width?: string;
  height?: string;
  position?: Position;
  role?: 'dialog' | 'alertdialog';
  hasBackdrop?: boolean;
  backdropClass?: string;
  disableClose?: boolean;
}
